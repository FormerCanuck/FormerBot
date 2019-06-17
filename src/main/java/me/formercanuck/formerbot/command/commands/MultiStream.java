package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class MultiStream extends Command {
    @Override
    public String getName() {
        return "multi";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        StringBuilder str = new StringBuilder();
        for (String s : args)
            str.append("/").append(s.replace("@", " ").trim());
        Main.getInstance().getBot().getChannel().messageChannel(String.format("https://multistre.am/%s%s/layout4/", Main.getInstance().getBot().getChannel().getChannelName(), str.toString().trim()));
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
