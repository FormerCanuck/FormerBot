package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;

import java.util.ArrayList;

public class Whitelist extends Command {

    private ConfigFile config;

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        config = Main.getInstance().getBot().getBotFile();
        Bot bot = Main.getInstance().getBot();
        if (config.getWhitelist() != null)
            Main.getInstance().getBot().setWhitelisted((ArrayList<String>) config.getWhitelist());

        if (Main.getInstance().getBot().isMod(sender) || bot.getWhitelisted().contains(sender.toLowerCase())) {
            if (args.size() > 0) {
                if (args.get(0).equalsIgnoreCase("add")) {
                    if (!bot.getWhitelisted().contains(args.get(1))) {
                        bot.getWhitelisted().add(args.get(1).toLowerCase());
                        config.set("whitelist", bot.getWhitelisted());
                        Main.getInstance().getBot().messageChannel(String.format("%s, successfully added %s to the whitelist", sender, args.get(1)));
                        return;
                    } else {
                        Main.getInstance().getBot().messageChannel(String.format("%s, %s is already whtelisted.", sender, args.get(1)));
                        return;
                    }
                } else if (args.get(0).equalsIgnoreCase("remove")) {
                    if (bot.getWhitelisted().contains(args.get(1).toLowerCase())) {
                        bot.getWhitelisted().remove(args.get(1).toLowerCase());
                        config.set("whitelist", bot.getWhitelisted());
                        Main.getInstance().getBot().messageChannel(String.format("%s, successfully removed %s from the whitelist", sender, args.get(1)));
                        return;
                    } else {
                        Main.getInstance().getBot().messageChannel(String.format("%s, %s isn't on the whtelisted.", sender, args.get(1)));
                        return;
                    }
                }
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
