package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.timertasks.CommandCooldown;

import java.util.Timer;

public abstract class Command {

    public abstract String getName();

    public abstract void onCommand(String sender, String channel, String... args);

    public abstract int getCooldown();

    protected void cooldown() {
        Main.getInstance().getBot().getCommandManager().addCooldown(getName());
        Timer timer = new Timer();
        timer.schedule(new CommandCooldown(getName()), (1000 * 60) * getCooldown());
    }
}
