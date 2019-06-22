package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

public class MultiStream extends Command {
    @Override
    public String getName() {
        return "multi";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 2; i++)
            str.append("/").append(args[i].replace("@", " ").trim());
        Main.getInstance().getBot().getChannel().messageChannel(String.format("https://multistre.am/%s%s/layout4/", Main.getInstance().getBot().getChannel().getChannelName(), str.toString().trim()));
    }

    @Override
    public String getUsage() {
        return String.format("Usage: !multi <user> (can specify up to 3) will give a link to view %s and the specified channels", Main.getInstance().getBot().getChannel().getChannelName());
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
