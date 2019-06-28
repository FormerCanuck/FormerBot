package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Listen extends Command {
    @Override
    public String getName() {
        return "listen";
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !listen <user> - Will take users chat and put it in yours using FormerB0t.";
    }

    @Override
    public void onCommand(String sender, Channel channel, String... args) {
        if (!channel.isMod(sender)) return;

        if (args.length == 0) channel.messageChannel(getUsage(channel));

        channel.toggleListen(args[0]);
        channel.messageChannel("Joining @" + args[0] + " to listen for messages.");
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
