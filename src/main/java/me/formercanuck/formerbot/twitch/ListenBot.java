package me.formercanuck.formerbot.twitch;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.connection.ListenToIRC;
import me.formercanuck.formerbot.connection.TwitchConnection;

public class ListenBot {

    private TwitchConnection twitchConnection;
    private ListenToIRC listenToIRC;

    private Thread thread;

    public ListenBot(String channel) {
        twitchConnection = Main.getInstance().getBot().getTwitchConnection();
        listenToIRC = new ListenToIRC();

        sendRawMessage("CAP REQ :twitch.tv/tags");
        sendRawMessage("CAP REQ :twitch.tv/commands");
        sendRawMessage("PASS oauth:3tphuo7t7zhoz0os9we3vpwcpfhxur");
        sendRawMessage("NICK FormerB0t");
        thread = new Thread(listenToIRC);
        thread.start();

        sendRawMessage("JOIN #" + channel);
    }

    public void stop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendRawMessage(String message) {
        try {
            twitchConnection.getToTwitch().write(String.format("%s %s", message, "\r\n"));
            twitchConnection.getToTwitch().flush();
        } catch (Exception e) {
        }
    }
}
