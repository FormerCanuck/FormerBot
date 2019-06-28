package me.formercanuck.formerbot.timertasks;

import me.formercanuck.formerbot.twitch.Channel;

import java.util.TimerTask;

public class CommandCooldown extends TimerTask {

    private String commandName;

    private Channel channel;

    public CommandCooldown(String commandName, Channel channel) {
        this.commandName = commandName;
        this.channel = channel;
    }

    @Override
    public void run() {
        channel.getCommandManager().cooldown.remove(commandName);
    }
}
