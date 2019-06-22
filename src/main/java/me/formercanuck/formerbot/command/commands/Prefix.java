package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

public class Prefix extends Command {
    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        Bot bot = Main.getInstance().getBot();
        if (bot.getChannel().isMod(sender)) {
            if (args.length == 0)
                bot.getChannel().messageChannel(sender + " Usage: " + bot.getBotFile().getString("prefix") + "prefix <new prefix>");
            if (args.length == 1) {
                bot.getBotFile().set("prefix", args[0]);
                bot.getChannel().messageChannel(String.format("%s has changed the prefix to %s", sender, args[0]));
            }
        }
    }

    @Override
    public String getUsage() {
        return "Usage: Mod > !prefix <new prefix> will change what the bot listens for to distinguish if a command is trying to be used.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
