package me.formercanuck.formerbot.timertasks;

import me.formercanuck.formerbot.Main;

import java.util.TimerTask;

public class CommandCooldown extends TimerTask {

    private String commandName;

    public CommandCooldown(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public void run() {
        Main.getInstance().getBot().getCommandManager().cooldown.remove(commandName);
    }
}
