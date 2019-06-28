package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.ArrayList;

public class Whitelist extends Command {

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        ConfigFile config = channel.getChannelFile();
        if (config.getWhitelist() != null)
            channel.setWhitelisted((ArrayList<String>) config.getWhitelist());

        if (channel.isMod(sender) || channel.isWhiteListed(sender)) {
            if (args.length > 0) {

                String user = args[0].replace("@", " ").toLowerCase().trim();

                if (!channel.isWhiteListed(user)) {
                    channel.getWhitelisted().add(user);
                    config.set("whitelist", channel.getWhitelisted());
                    channel.messageChannel(String.format("%s, successfully added %s to the whitelist", sender, args[0]));
                    return;
                }

                if (channel.getWhitelisted().contains(user)) {
                    channel.getWhitelisted().remove(user);
                    config.set("whitelist", channel.getWhitelisted());
                    channel.messageChannel(String.format("%s, successfully removed %s from the whitelist", sender, args[0]));
                }
            } else {
                channel.messageChannel("Usage: !whitelist <add | remove> <username>");
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: Mod Only > !whitelist <user> will add a user to the whitelist.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
