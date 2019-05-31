package me.formercanuck.formerbot;

import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.connection.TwitchConnection;

public class Bot {

    private TwitchConnection twitchConnection;

    private ReadTwitchIRC readTwitchIRC;

    private CommandManager commandManager;

    private String channel;

    private final String CLIENT_ID = "pqi99elyam4p8ewyab8eyrxnb8urvw";

    public Bot() {
        twitchConnection = new TwitchConnection();
        readTwitchIRC = new ReadTwitchIRC(twitchConnection);
        commandManager = new CommandManager();
    }

    public void connect() {
        sendRawMessage("CAP REQ :twitch.tv/tags");
        sendRawMessage("PASS oauth:3tphuo7t7zhoz0os9we3vpwcpfhxur");
        sendRawMessage("NICK FormerB0t");

        new Thread(readTwitchIRC).start();
    }

    public void joinChannel(String channel) {
        this.channel = channel;
        sendRawMessage("JOIN " + channel);
    }

    public TwitchConnection getTwitchConnection() {
        return twitchConnection;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public boolean messageChannel(String message) {
        return sendRawMessage("PRIVMSG " + channel + " :" + message);
    }

    public boolean sendRawMessage(String message) {
        try {
            Main.getInstance().getConsole().println("> " + message);
            twitchConnection.getToTwitch().write(String.format("%s %s", message, "\r\n"));
            twitchConnection.getToTwitch().flush();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
