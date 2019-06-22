package me.formercanuck.formerbot.connection;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Bot;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ReadTwitchIRC implements Runnable {

    private TwitchConnection twitchConnection;

    private int mins;
    private int secs;
    private Timer clearTimer;
    private boolean aboutToClear = false;
    private Bot bot;
    private boolean newLineSinceClear;

    public ReadTwitchIRC(TwitchConnection twitchConnection) {
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

                if (line.contains("End of /NAMES list")) {
                    bot.sendRawMessage(String.format("PRIVMSG %s :%s", bot.getChannel().getChannel(), "/mods"));
                }

                if (line.contains("The moderators of this channel are:")) {
                    String[] ln = line.split(":");
                    String[] mods = ln[3].split(",");

                    bot.getChannel().addMod("formercanuck");
                    bot.getChannel().addMod(bot.getChannel().getChannelName());

                    for (String mod : mods) {
                        bot.getChannel().addMod(mod.replace(",", "").substring(1));
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

                    newLineSinceClear = true;
                    String msg = stringBuilder.toString().substring(1);

                    if (msg.startsWith(bot.getBotFile().getString("prefix"))) {
                        String command = stringBuilder.substring(2, stringBuilder.indexOf(" "));
                        stringBuilder.delete(0, stringBuilder.indexOf(" "));
                        String[] args = new String[0];
                        if (!stringBuilder.substring(1).trim().isEmpty())
                            args = stringBuilder.substring(1).trim().split(" ");
                        bot.getCommandManager().onCommand(user, channel, command, args);
                    } else if (msg.startsWith("/")) {
                    } else {
                        if (bot.getBotFile().getBoolean("autoClear") && newLineSinceClear) {
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

                                    if (remaining == (60 * 5)) {
                                        bot.getChannel().messageChannel("Clearing chat in 5 minutes type anything to cancel.");
                                        aboutToClear = true;
                                    }

                                    if (remaining == 1) {
                                        bot.sendRawMessage(String.format("PRIVMSG %s :%s", bot.getChannel().getChannel(), "/clear"));
                                        newLineSinceClear = false;
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
        try {
            if (clearTimer != null)
                clearTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (aboutToClear) {
            bot.getChannel().messageChannel("Clear has been cancelled.");
            aboutToClear = false;
        }
    }
}
