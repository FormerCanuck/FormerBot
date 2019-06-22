package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

public class Disable extends Command {
    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        Bot bot = Main.getInstance().getBot();

        if (bot.getChannel().isMod(sender)) {
            if (bot.getCommandManager().disableCommand(args[0])) {
                bot.getChannel().messageChannel(String.format("%s has disabled %s", sender, args[0]));
            }
        }
    }

    @Override
    public String getUsage() {
        return "Usage: !disable <command name> will disable given command until !enable <command> is run.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
