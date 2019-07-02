package me.formercanuck.formerbot.twitch;

import me.fc.console.Console;
import me.formercanuck.formerbot.FormerConsole;
import me.formercanuck.formerbot.connection.TwitchConnection;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Bot {

    private TwitchConnection twitchConnection;

    private ArrayList<String> remember = new ArrayList<>();

    private HashMap<String, Channel> channels;

    private Console console;

    public Bot() {
        this.console = new FormerConsole("FormerB0t Console");
        this.channels = new HashMap<>();
        twitchConnection = new TwitchConnection();
    }

    public void connect() {
        sendRawMessage("CAP REQ :twitch.tv/tags");
        sendRawMessage("CAP REQ :twitch.tv/commands");
        sendRawMessage("PASS oauth:3tphuo7t7zhoz0os9we3vpwcpfhxur");
        sendRawMessage("NICK FormerB0t");
    }

    public void joinChannel(String channel) {
        Channel temp = new Channel(channel);
        temp.join();
        this.channels.put(temp.getChannelName().toLowerCase(), temp);
    }

    /**
     * @param channel - The name of the channel you are trying to get ex: FormerCanuck.
     * @return - Returns the Channel object with the name given.
     */
    public Channel getChannel(String channel) {
        if (channel.startsWith("#")) {
            return channels.get(channel.substring(1).toLowerCase());
        } else return channels.get(channel.toLowerCase());
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
            console.println("> " + message, Color.YELLOW);
            twitchConnection.getToTwitch().write(String.format("%s %s", message, "\r\n"));
            twitchConnection.getToTwitch().flush();
        } catch (Exception e) {
            console.error(e.getLocalizedMessage());
        }
    }

    TwitchConnection getTwitchConnection() {
        return twitchConnection;
    }

    public Console getConsole() {
        return console;
    }
}
