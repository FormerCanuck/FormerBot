package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

import java.util.ArrayList;

public class Disable extends Command {
    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();

        if (bot.isMod(sender)) {
            if (bot.getCommandManager().disableCommand(args.get(0))) {
                bot.messageChannel(String.format("%s has disabled %s", sender, args.get(0)));
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
