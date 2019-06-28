package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Disable extends Command {
    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        if (channel.isMod(sender)) {
            if (channel.getCommandManager().disableCommand(args[0], channel)) {
                channel.messageChannel(String.format("%s has disabled %s", sender, args[0]));
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !disable <command name> will disable given command until !enable <command> is run.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
