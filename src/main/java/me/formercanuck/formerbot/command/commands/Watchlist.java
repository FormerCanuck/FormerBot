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
    public void onCommand(String sender, Channel channel, String[] args) {
        ConfigFile config = channel.getChannelFile();
        Bot bot = Main.getInstance().getBot();

        if (config.getWhitelist() != null)
            channel.setWatchList((ArrayList<String>) config.getWatchList());

        if (channel.isMod(sender)) {
            if (args.length > 0) {

                String user = args[0].replace("@", " ").toLowerCase().trim();

                if (!channel.onWatchlist(user) && !user.equalsIgnoreCase("formercanuck") && !user.equalsIgnoreCase(channel.getChannelName())) {
                    channel.getWatchList().add(user);
                    config.set("watchlist", channel.getWatchList());
                    channel.messageChannel(String.format("%s, successfully added %s to the watch list", sender, args[0]));
                    return;
                }

                if (channel.getWatchList().contains(user) && !user.equalsIgnoreCase("Fortressamerca1776")) {
                    channel.getWatchList().remove(user);
                    config.set("watchlist", channel.getWatchList());
                    channel.messageChannel(String.format("%s, successfully removed %s from the watch list", sender, args[0]));
                }
            } else {
                StringBuilder str = new StringBuilder();

                for (String s : channel.getWatchList()) str.append(s).append(", ");
                channel.messageChannel("Here is the list of users to watch: " + str.toString().trim());
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "NOT YET IMPLEMENTED TO LAZY ATM.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
