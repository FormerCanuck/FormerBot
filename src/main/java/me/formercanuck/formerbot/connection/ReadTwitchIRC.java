package me.formercanuck.formerbot.connection;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.MiscUtils;

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

    private Channel channel;

    public ReadTwitchIRC(TwitchConnection twitchConnection, Channel channel) {
        this.twitchConnection = twitchConnection;
        this.channel = channel;
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
                    bot.sendRawMessage(String.format("PRIVMSG %s :%s", channel.getChannel(), "/mods"));
                }

                if (line.contains("The moderators of this channel are:")) {
                    String[] ln = line.split(":");
                    String[] mods = ln[3].split(",");

                    channel.addMod("formercanuck");
                    channel.addMod(channel.getChannelName());

                    for (String mod : mods) {
                        channel.addMod(mod.replace(",", "").substring(1));
                    }
                }

                if (line.contains("PRIVMSG")) {
                    String[] ln = line.split(" ");
                    String user = line.substring(line.indexOf("name=") + 5, line.indexOf(";emotes"));
                    Color color = Color.decode(line.substring(line.indexOf("color=") + 6, line.indexOf(";display")));
                    String channel1 = ln[3];
                    Channel channel = bot.getChannel(channel1);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 4; i < ln.length; i++) {
                        stringBuilder.append(ln[i]).append(" ");
                    }

                    if (!channel.getHasChatted().contains(user) && channel.isFollowing(user) && channel.getChannelFile().getBoolean("shouldWelcome")) {
                        int noOfDays = Math.toIntExact(MiscUtils.numberOfDaysBetweenDateAndNow(channel.getFollowDate(user)));
                        int month = noOfDays / 30;
                        int week = month % 30;
                        int days = week % 7;

                        if (month > 0)
                            channel.messageChannel(String.format("o/ @%s, welcome back thanks for following for %s months.", user, month));
                        else
                            channel.messageChannel(String.format("o/ @%s, welcome back thanks for following.", user, month));
                        channel.addChatted(user);
                    }

                    newLineSinceClear = true;
                    String msg = stringBuilder.toString().substring(1);

                    if (msg.startsWith(channel.getChannelFile().getString("prefix"))) {
                        String command = stringBuilder.substring(2, stringBuilder.indexOf(" "));
                        stringBuilder.delete(0, stringBuilder.indexOf(" "));
                        String[] args = new String[0];
                        if (!stringBuilder.substring(1).trim().isEmpty())
                            args = stringBuilder.substring(1).trim().split(" ");
                        if (channel.getCommandManager() == null) System.exit(0);
                        channel.getCommandManager().onCommand(user, channel, command, args);
                    } else if (msg.startsWith("/")) {
                    } else {
                        if (channel.getChannelFile().getBoolean("autoClear") && newLineSinceClear && channel.isLive()) {
                            cancelClear();
                            clearTimer = new Timer();

                            TimerTask task = new TimerTask() {
                                int seconds = 60 * channel.getChannelFile().getInt("autoClearTime");
                                int remaining;
                                int i = 0;

                                @Override
                                public void run() {
                                    i++;

                                    remaining = ((seconds - (i % seconds)));
                                    mins = ((remaining % 86400) % 3600) / 60;
                                    secs = ((remaining) % 3600) % 60;

                                    if (remaining == (60 * 5)) {
                                        channel.messageChannel("Clearing chat in 5 minutes type anything to cancel.");
                                        aboutToClear = true;
                                    }

                                    if (remaining == 1) {
                                        bot.sendRawMessage(String.format("PRIVMSG %s :%s", channel.getChannel(), "/clear"));
                                        newLineSinceClear = false;
                                    }
                                }
                            };

                            clearTimer.schedule(task, 0, 1000);
                        }
                    }
                    if (color != null)
                        channel.getConsole().print(String.format("[%s][%s]: %s", channel.getChannel(), user, msg), user, Color.ORANGE, color);
                    else
                        channel.getConsole().println(String.format("[%s][%s]: %s", channel.getChannel(), user, msg), Color.ORANGE);

//                    if (color != null)
//                        channel.getConsole().println(String.format("[%s][%s]: %s", channel.getChannel(), user, msg), color);
//                    else
//                        channel.getConsole().println(String.format("[%s][%s]: %s", channel.getChannel(), user, msg), Color.ORANGE);
                }
                Main.getInstance().getConsole().println("< " + line, Color.MAGENTA);
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
            channel.messageChannel("Clear has been cancelled.");
            aboutToClear = false;
        }
    }
}
