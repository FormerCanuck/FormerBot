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
            int amt;

            try {
                amt = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                channel.messageChannel(String.format("@%s, %s is an invalid number or to large.", sender, args[0]));
                return;
            }

            if (channel.getPoints(sender) >= amt) {
                String user = args[1].toLowerCase().replace("@", " ").trim();
                if (channel.getPoints(user) > amt) {
                    channel.messageChannel(String.format("%s has challenged %s to a duel. To accept type %saccept", sender, user, channel.getChannelFile().get("prefix")));
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
