package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

import java.util.ArrayList;

public class Prefix extends Command {
    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        if (bot.isMod(sender)) {
            if (args.size() == 0)
                bot.messageChannel(sender + " Usage: " + bot.getBotFile().getString("prefix") + "prefix <new prefix>");
            if (args.size() == 1) {
                bot.getBotFile().set("prefix", args.get(0));
                bot.messageChannel(String.format("%s has changed the prefix to %s", sender, args.get(0)));
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
