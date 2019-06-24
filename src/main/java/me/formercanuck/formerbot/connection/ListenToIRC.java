package me.formercanuck.formerbot.connection;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Bot;

import java.awt.*;
import java.io.IOException;

public class ListenToIRC implements Runnable {

    private TwitchConnection twitchConnection;

    private Bot bot;

    public ListenToIRC(TwitchConnection twitchConnection) {
        this.twitchConnection = twitchConnection;
    }

    public void run() {
        bot = Main.getInstance().getBot();
        String line;

        try {
            while ((line = twitchConnection.getFromTwitch().readLine()) != null) {
                if (line.equalsIgnoreCase("PING :tmi.twitch.tv")) {
                    bot.sendRawMessage("PONG :tmi.twitch.tv");
                }

                if (line.contains("PRIVMSG")) {
                    String[] ln = line.split(" ");
                    String user = line.substring(line.indexOf("name=") + 5, line.indexOf(";emotes"));
                    String channel = ln[3];
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 4; i < ln.length; i++) {
                        stringBuilder.append(ln[i]).append(" ");
                    }

                    String msg = stringBuilder.toString().substring(1);

                    if (!channel.equalsIgnoreCase(bot.getChannel().getChannel()))
                        Main.getInstance().getBot().getChannel().messageChannel(String.format("[%s][%s]: %s", channel, user, msg));

                    Main.getInstance().getConsole().println(String.format("[%s][%s]: %s", channel, user, msg), Color.GREEN);
                }
            }
        } catch (IOException ignored) {

        }
    }
}
