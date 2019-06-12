package me.formercanuck.formerbot.connection;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ReadTwitchIRC implements Runnable {

    private TwitchConnection twitchConnection;

    public int mins;
    public int secs;
    private Timer clearTimer;
    private boolean aboutToClear = false;
    private Bot bot;

    public ReadTwitchIRC(TwitchConnection twitchConnection) {
        this.twitchConnection = twitchConnection;
    }

    public void run() {
        bot = Main.getInstance().getBot();
        String line;

        try {
            while ((line = twitchConnection.getFromTwitch().readLine()) != null) {
                Main.getInstance().getConsole().println(line);

                if (line.equalsIgnoreCase("PING :tmi.twitch.tv")) {
                    bot.sendRawMessage("PONG :tmi.twitch.tv");
                }

                if (line.contains("End of /NAMES list")) {
                    bot.messageChannel("/mods");
                }

                if (line.contains("The moderators of this channel are:")) {
                    String[] ln = line.split(":");
                    String[] mods = ln[3].split(",");

                    bot.addMod("formercanuck");
                    bot.addMod(bot.getChannel().substring(1));

                    for (String mod : mods) {
                        bot.addMod(mod.replace(",", "").substring(1));
                    }
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

                    if (msg.startsWith("!")) {
                        String command = stringBuilder.substring(2, stringBuilder.indexOf(" "));
                        stringBuilder.delete(0, stringBuilder.indexOf(" "));
                        stringBuilder.substring(1);
                        ArrayList<String> args = new ArrayList<>(Arrays.asList(stringBuilder.toString().split(" ")));
                        if (args.size() > 0)
                            args.remove(0);
                        bot.getCommandManager().onCommand(user, channel, command, args);
                    } else if (msg.startsWith("/")) {
                    } else {
                        if (bot.getBotFile().getBoolean("autoClear")) {
                            cancelClear();
                            clearTimer = new Timer();

                            TimerTask task = new TimerTask() {
                                int seconds = 60 * bot.getBotFile().getInt("autoClearTime");
                                int remaining;
                                int i = 0;

                                @Override
                                public void run() {
                                    i++;

                                    remaining = ((seconds - (i % seconds)));
                                    mins = ((remaining % 86400) % 3600) / 60;
                                    secs = ((remaining) % 3600) % 60;

                                    if (remaining == 60) {
                                        bot.messageChannel("Clearing chat in 1 minute type anything to cancel.");
                                        aboutToClear = true;
                                    }

                                    System.out.println(remaining);

                                    if (remaining == 1) {
                                        bot.messageChannel("/clear");
                                    }
                                }
                            };

                            clearTimer.schedule(task, 0, 1000);
                        }
                    }

                    Main.getInstance().getConsole().println(String.format("[%s][%s]: %s", channel, user, msg), Color.GREEN);
                }
            }
        } catch (IOException ignored) {

        }
    }

    public String getTimeRemaining() {
        if (mins > 0)
            return String.format("%s minutes and %s seconds", mins, secs);
        else
            return String.format("%s seconds", secs);
    }

    public void cancelClear() {
        if (aboutToClear) {
            try {
                clearTimer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bot.messageChannel("Clear has been cancelled.");
            aboutToClear = false;
        }
    }
}
