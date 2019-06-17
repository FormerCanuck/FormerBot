package me.formercanuck.formerbot.twitch;

import me.fc.console.Console;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.connection.TwitchConnection;
import me.formercanuck.formerbot.files.ConfigFile;

import java.util.ArrayList;

public class Bot {

    private TwitchConnection twitchConnection;

    private ReadTwitchIRC readTwitchIRC;

    private CommandManager commandManager;

    private ArrayList<String> remember = new ArrayList<>();

    private Channel channel;

    private ConfigFile botFile;

    private Console console;

    public Bot() {
        twitchConnection = new TwitchConnection();
        readTwitchIRC = new ReadTwitchIRC(twitchConnection);
        console = Main.getInstance().getConsole();
    }

    public void connect() {
        sendRawMessage("CAP REQ :twitch.tv/tags");
        sendRawMessage("CAP REQ :twitch.tv/commands");
        sendRawMessage("PASS oauth:3tphuo7t7zhoz0os9we3vpwcpfhxur");
        sendRawMessage("NICK FormerB0t");

        new Thread(readTwitchIRC).start();
    }

    public void joinChannel(String channel) {
        this.channel = new Channel(channel);

        botFile = new ConfigFile(this.channel.getChannelName());

        if (!botFile.contains("prefix")) botFile.set("prefix", "!");

        if (!botFile.contains("autoClear")) {
            botFile.set("autoClear", false);
            botFile.set("autoClearTime", 10);
        }
        commandManager = new CommandManager();

        this.channel.loadFollows();
    }

    public ReadTwitchIRC getReadTwitchIRC() {
        return readTwitchIRC;
    }

    public ConfigFile getBotFile() {
        return botFile;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Channel getChannel() {
        return channel;
    }

    public void addRemember(String str) {
        this.remember.add(str);
    }

    public boolean isRememberEmpty() {
        return remember.isEmpty();
    }

    public String getRemember() {
        String temp = remember.get(0);
        remember.remove(0);
        return temp;
    }

    public void sendRawMessage(String message) {
        try {
            console.println("> " + message);
            twitchConnection.getToTwitch().write(String.format("%s %s", message, "\r\n"));
            twitchConnection.getToTwitch().flush();
        } catch (Exception e) {
            console.error(e.toString());
        }
    }
}
