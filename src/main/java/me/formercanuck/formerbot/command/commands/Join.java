package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

public class Join extends Command {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getUsage() {
        return "Usage: !join - Makes the bot listen for commands.";
    }

    @Override
    public void onCommand(String sender, String channel, String... args) {
        Main.getInstance().getBot().getChannel().toggleListen(true);
        Main.getInstance().getBot().getChannel().messageChannel("\\o/ Guess who's here!");
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
