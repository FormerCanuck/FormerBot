package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;

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
        Channel chnl = bot.getChannel();

        if (config.getWhitelist() != null)
            chnl.setWatchList((ArrayList<String>) config.getWatchList());

        if (chnl.isMod(sender) || chnl.onWatchlist(sender)) {
            if (args.size() > 0) {

                String user = args.get(0).replace("@", " ").toLowerCase().trim();

                if (!chnl.onWatchlist(user)) {
                    chnl.getWatchList().add(user);
                    config.set("watchlist", chnl.getWatchList());
                    Main.getInstance().getBot().getChannel().messageChannel(String.format("%s, successfully added %s to the watch list", sender, args.get(0)));
                    return;
                }

                if (chnl.getWatchList().contains(user)) {
                    chnl.getWatchList().remove(user);
                    config.set("watchlist", chnl.getWatchList());
                    Main.getInstance().getBot().getChannel().messageChannel(String.format("%s, successfully removed %s from the watch list", sender, args.get(0)));
                }
            } else {
                StringBuilder str = new StringBuilder();

                for (String s : chnl.getWatchList()) str.append(s).append(", ");
                bot.getChannel().messageChannel("Here is the list of users to watch: " + str.toString().trim());
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
