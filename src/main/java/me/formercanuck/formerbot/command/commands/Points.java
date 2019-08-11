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
        if (args.length == 0 || !channel.isMod(sender) && channel.getPointsMap().containsKey(sender.toLowerCase())) {
            channel.whisper(sender, String.format("You have %s points.", channel.getPointsMap().get(sender.toLowerCase())));
        } else if (args.length < 3) {
            channel.messageChannel(getUsage(channel));
        } else {
            String cmd = args[0];
            int amt;

            try {
                amt = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                channel.messageChannel(String.format("@%s, %s is an invalid number or to large.", sender, args[1]));
                return;
            }

            String user = args[2].toLowerCase().replace("@", " ").trim();

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
