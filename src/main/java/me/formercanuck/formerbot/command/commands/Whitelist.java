package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.twitch.Bot;

import java.util.ArrayList;

public class Whitelist extends Command {

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        ConfigFile config = Main.getInstance().getBot().getBotFile();
        Bot bot = Main.getInstance().getBot();
        if (config.getWhitelist() != null)
            Main.getInstance().getBot().getChannel().setWhitelisted((ArrayList<String>) config.getWhitelist());

        if (Main.getInstance().getBot().getChannel().isMod(sender) || bot.getChannel().isWhiteListed(sender)) {
            if (args.length > 0) {

                String user = args[0].replace("@", " ").toLowerCase().trim();

                if (!bot.getChannel().isWhiteListed(user)) {
                    bot.getChannel().getWhitelisted().add(user);
                    config.set("whitelist", bot.getChannel().getWhitelisted());
                    Main.getInstance().getBot().getChannel().messageChannel(String.format("%s, successfully added %s to the whitelist", sender, args[0]));
                    return;
                }

                if (bot.getChannel().getWhitelisted().contains(user)) {
                    bot.getChannel().getWhitelisted().remove(user);
                    config.set("whitelist", bot.getChannel().getWhitelisted());
                    Main.getInstance().getBot().getChannel().messageChannel(String.format("%s, successfully removed %s from the whitelist", sender, args[0]));
                }
            } else {
                bot.getChannel().messageChannel("Usage: !whitelist <add | remove> <username>");
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
