package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

public class Enable extends Command {
    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        Bot bot = Main.getInstance().getBot();

        if (bot.getChannel().isMod(sender)) {
            if (bot.getCommandManager().enableCommand(args[0]))
                bot.getChannel().messageChannel(String.format("%s has enabled %s", sender, args[0]));
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
