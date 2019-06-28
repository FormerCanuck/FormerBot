package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Leave extends Command {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        channel.messageChannel("Okay @" + sender + " o/");
        channel.toggleListen(false);
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !leave will make the bot leave the channel. (Currently unless Former is around no way to get the bot back in the channel)";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
