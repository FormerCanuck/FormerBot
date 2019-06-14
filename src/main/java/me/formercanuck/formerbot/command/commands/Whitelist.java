package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;

import java.util.ArrayList;

public class Whitelist extends Command {

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        ConfigFile config = Main.getInstance().getBot().getBotFile();
        Bot bot = Main.getInstance().getBot();
        if (config.getWhitelist() != null)
            Main.getInstance().getBot().setWhitelisted((ArrayList<String>) config.getWhitelist());

        if (Main.getInstance().getBot().isMod(sender) || bot.isWhiteListed(sender)) {
            if (args.size() > 0) {

                String user = args.get(0).replace("@", " ").toLowerCase().trim();

                if (!bot.isWhiteListed(user)) {
                    bot.getWhitelisted().add(user);
                    config.set("whitelist", bot.getWhitelisted());
                    Main.getInstance().getBot().messageChannel(String.format("%s, successfully added %s to the whitelist", sender, args.get(0)));
                    return;
                }

                if (bot.getWhitelisted().contains(user)) {
                    bot.getWhitelisted().remove(user);
                    config.set("whitelist", bot.getWhitelisted());
                    Main.getInstance().getBot().messageChannel(String.format("%s, successfully removed %s from the whitelist", sender, args.get(0)));
                }
            } else {
                bot.messageChannel("Usage: !whitelist <add | remove> <username>");
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
