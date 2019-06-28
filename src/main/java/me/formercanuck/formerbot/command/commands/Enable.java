package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Enable extends Command {
    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        if (channel.isMod(sender)) {
            if (channel.getCommandManager().enableCommand(args[0], channel))
                channel.messageChannel(String.format("%s has enabled %s", sender, args[0]));
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !enable <command name> will enable a disabled command.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
