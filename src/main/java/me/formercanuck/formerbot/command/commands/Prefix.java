package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;

public class Prefix extends Command {
    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        Bot bot = Main.getInstance().getBot();
        if (channel.isMod(sender)) {
            if (args.length == 0)
                channel.messageChannel(sender + " Usage: " + channel.getChannelFile().getString("prefix") + "prefix <new prefix>");
            if (args.length == 1) {
                channel.getChannelFile().set("prefix", args[0]);
                channel.messageChannel(String.format("%s has changed the prefix to %s", sender, args[0]));
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: Mod > !prefix <new prefix> will change what the bot listens for to distinguish if a command is trying to be used.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
