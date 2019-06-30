package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.timertasks.DuelTask;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.Timer;

public class Duel extends Command {
    @Override
    public String getName() {
        return "duel";
    }

    @Override
    public String getUsage(Channel channel) {
        return "!duel <amount> <user>";
    }

    @Override
    public void onCommand(String sender, Channel channel, String... args) {
        if (args.length < 2) {
            channel.messageChannel(getUsage(channel));
        } else {
            int amt = Integer.parseInt(args[0]);
            if (channel.getPoints(sender) > amt) {
                String user = args[1].toLowerCase();
                if (channel.getPoints(user) > amt) {
                    channel.messageChannel(String.format("%s has challenged %s to a duel. To accept type !accept", sender, user));
                    DuelTask duelTask = new DuelTask(channel, sender, user, amt);
                    channel.addDuel(duelTask);
                    Timer timer = new Timer();
                    timer.schedule(duelTask, 60 * 1000);
                } else {
                    channel.messageChannel(String.format("%s doesn't have enough points to duel.", user));
                }
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
