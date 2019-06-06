package me.formercanuck.formerbot.connection;

import me.formercanuck.formerbot.Main;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadTwitchIRC implements Runnable {

    private TwitchConnection twitchConnection;

    public ReadTwitchIRC(TwitchConnection twitchConnection) {
        this.twitchConnection = twitchConnection;
    }

    public void run() {
        String line = null;

        try {
            while ((line = twitchConnection.getFromTwitch().readLine()) != null) {
                Main.getInstance().getConsole().info(line);

                if (line.equalsIgnoreCase("PING :tmi.twitch.tv")) {
                    Main.getInstance().getBot().sendRawMessage("PONG :tmi.twitch.tv");
                }

                if (line.contains("End of /NAMES list")) {
                    Main.getInstance().getBot().messageChannel("/mods");
                }

                if (line.contains("The moderators of this channel are:")) {
                    String[] ln = line.split(":");
                    String[] mods = ln[3].split(",");

                    Main.getInstance().getBot().addMod("formercanuck");
                    Main.getInstance().getBot().addMod(Main.getInstance().getBot().getChannel().substring(1));

                    for (int i = 0; i < mods.length; i++) {
                        Main.getInstance().getBot().addMod(mods[i].replace(",", "").substring(1));
                    }
                }

                if (line.contains("PRIVMSG")) {
                    String[] ln = line.split(" ");
                    String user = line.substring(line.indexOf("name=") + 5, line.indexOf(";emotes"));
                    String channel = ln[3];
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 4; i < ln.length; i++) {
                        stringBuilder.append(ln[i] + " ");
                    }

                    String msg = stringBuilder.toString().substring(1);

                    if (msg.startsWith("!")) {
                        String command = stringBuilder.substring(2, stringBuilder.indexOf(" "));
                        stringBuilder.delete(0, stringBuilder.indexOf(" "));
                        stringBuilder.substring(1);
                        ArrayList<String> args = new ArrayList<String>(Arrays.asList(stringBuilder.toString().split(" ")));
                        if (args.size() > 0)
                            args.remove(0);
                        Main.getInstance().getBot().getCommandManager().onCommand(user, channel, command, args);
                    }

                    Main.getInstance().getConsole().println(String.format("[%s][%s]: %s", channel, user, msg), Color.GREEN);
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
