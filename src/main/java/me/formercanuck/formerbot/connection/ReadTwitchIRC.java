package me.formercanuck.formerbot.connection;

import me.formercanuck.formerbot.Main;

import java.awt.*;
import java.io.IOException;

public class ReadTwitchIRC implements Runnable {

    private TwitchConnection twitchConnection;

    public ReadTwitchIRC(TwitchConnection twitchConnection) {
        this.twitchConnection = twitchConnection;
    }

    public void run() {
        String line = null;

        try {
            while ((line = twitchConnection.getFromTwitch().readLine()) != null) {
                Main.getInstance().getConsole().println(line, Color.GREEN);

                // ce
                //:annedorko!annedorko@annedorko.tmi.twitch.tv PRIVMSG #ulincsys :blobDance
                if (line.contains("PRIVMSG")) {
                    String[] ln = line.split(" ");
                    String user = line.substring(line.indexOf("name=") + 5, line.indexOf(";emotes") - 6);
                    String channel = ln[3];
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 4; i < ln.length; i++) {
                        stringBuilder.append(ln[i] + " ");
                    }

                    String msg = stringBuilder.toString().substring(1);

                    if (msg.startsWith("!")) {

                    }

                    System.out.println(line);

                    Main.getInstance().getConsole().println(String.format("[%s][%s]: %s", channel, user, msg), Color.RED);
                }
            }
        } catch (IOException e) {

        }
    }

    private Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
