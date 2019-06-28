package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Link extends Command {
    @Override
    public String getName() {
        return "link";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        if (channel.getChannelFile().getWhitelist().contains(sender.toLowerCase())) {
            channel.messageChannel("!allow " + sender);
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: Whitelisted-User > !link will grant you permission to post a link.";
    }

    @Override
    public int getCooldown() {
        return 5;
    }
}
