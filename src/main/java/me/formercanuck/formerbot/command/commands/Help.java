package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

public class Help extends Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        StringBuilder str = new StringBuilder();
        int size =

        for (Command cmd : Main.getInstance().getBot().getCommandManager().getCommands()) {
            str.append(", ").append(cmd.getName());
        }

        Main.getInstance().getBot().getChannel().messageChannel(String.format("%s, here is a list of my commands: %s", sender, str.toString().substring(2)));
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
