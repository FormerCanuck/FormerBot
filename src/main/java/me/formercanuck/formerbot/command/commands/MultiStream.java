package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class MultiStream extends Command {
    @Override
    public String getName() {
        return "multi";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 2; i++)
            str.append("/").append(args[i].replace("@", " ").trim());
        channel.messageChannel(String.format("https://multistre.am/%s%s/layout4/", channel.getChannelName(), str.toString().trim()));
    }

    @Override
    public String getUsage(Channel channel) {
        return String.format("Usage: !multi <user> (can specify up to 3) will give a link to view %s and the specified channels", channel.getChannelName());
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
