package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class Link extends Command {
    @Override
    public String getName() {
        return "link";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        if (Main.getInstance().getBot().getBotFile().getWhitelist().contains(sender.toLowerCase())) {
            Main.getInstance().getBot().getChannel().messageChannel("!allow " + sender);
        }
    }

    @Override
    public int getCooldown() {
        return 5;
    }
}
