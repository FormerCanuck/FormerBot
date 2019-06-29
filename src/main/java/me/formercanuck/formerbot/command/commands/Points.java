package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Points extends Command {
    @Override
    public String getName() {
        return "points";
    }

    @Override
    public String getUsage(Channel channel) {
        return "!points Mods > <add | remove> amount user";
    }

    @Override
    public void onCommand(String sender, Channel channel, String... args) {
        if (args.length == 0 || !channel.isMod(sender) && channel.getPoints().containsKey(sender.toLowerCase())) {
            channel.messageChannel(String.format("%s you have %s points.", sender, channel.getPoints().get(sender.toLowerCase())));
        } else if (args.length < 3) {
            channel.messageChannel(getUsage(channel));
        } else {
            String cmd = args[0];
            int amt = Integer.parseInt(args[1]);
            String user = args[2].toLowerCase();

            if (cmd.equalsIgnoreCase("add")) {
                channel.addPoints(user, amt);
                channel.messageChannel(String.format("%s has added %s point(s) to %s", sender, amt, user));
            } else if (cmd.equalsIgnoreCase("remove")) {
                channel.removePoints(user, amt);
                channel.messageChannel(String.format("%s has removed %s point(s) to %s", sender, amt, user));
            }
        }

    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
