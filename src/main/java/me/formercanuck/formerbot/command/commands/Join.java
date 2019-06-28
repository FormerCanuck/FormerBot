package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Join extends Command {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !join - Makes the bot listen for commands.";
    }

    @Override
    public void onCommand(String sender, Channel channel, String... args) {
        channel.toggleListen(true);
        channel.messageChannel("\\o/ Guess who's here!");
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
