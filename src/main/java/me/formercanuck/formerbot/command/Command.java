package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.timertasks.CommandCooldown;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.Timer;

public abstract class Command {

    public abstract String getName();

    public abstract String getUsage(Channel channel);

    public abstract void onCommand(String sender, Channel channel, String... args);

    public abstract int getCooldown();

    protected void cooldown(Channel channel) {
        channel.getCommandManager().addCooldown(getName());
        Timer timer = new Timer();
        timer.schedule(new CommandCooldown(getName(), channel), (1000 * 60) * getCooldown());
    }
}
