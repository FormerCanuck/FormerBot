package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Pals extends Command {
    @Override
    public String getName() {
        return "pals";
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !pals add | remove <user> will add user to pals list.";
    }

    @Override
    public void onCommand(String sender, Channel channel, String... args) {
        if (channel.isMod(sender)) {
            if (args.length < 2) {
                channel.messageChannel(getUsage(channel));
            } else {
                if (args[0].equalsIgnoreCase("add")) {
                    if (channel.getChannelFile().addPal(args[1]))
                        channel.messageChannel(String.format("%s has added %s to the list of pals.", sender, args[1]));
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (channel.getChannelFile().removePal(args[1]))
                        channel.messageChannel(String.format("%s has removed %s from the list of pals.", sender, args[1]));
                } else
                    channel.messageChannel(getUsage(channel));
            }
        }
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
