package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.twitch.Bot;

import java.util.ArrayList;

public class Watchlist extends Command {

    @Override
    public String getName() {
        return "watchlist";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        ConfigFile config = Main.getInstance().getBot().getBotFile();
        Bot bot = Main.getInstance().getBot();
        if (config.getWhitelist() != null)
            Main.getInstance().getBot().setWatchList((ArrayList<String>) config.getWatchList());

        if (Main.getInstance().getBot().isMod(sender) || bot.onWatchlist(sender)) {
            if (args.size() > 0) {

                String user = args.get(0).replace("@", " ").toLowerCase().trim();

                if (!bot.onWatchlist(user)) {
                    bot.getWhitelisted().add(user);
                    config.set("watchlist", bot.getWhitelisted());
                    Main.getInstance().getBot().messageChannel(String.format("%s, successfully added %s to the watch list", sender, args.get(0)));
                    return;
                }

                if (bot.getWhitelisted().contains(user)) {
                    bot.getWhitelisted().remove(user);
                    config.set("watchlist", bot.getWhitelisted());
                    Main.getInstance().getBot().messageChannel(String.format("%s, successfully removed %s from the watch list", sender, args.get(0)));
                }
            } else {
                StringBuilder str = new StringBuilder();

                for (String s : bot.getWatchList()) str.append(s).append(", ");
                bot.messageChannel("Here is the list of users to watch: " + str.toString().trim());
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
