package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class Help extends Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Main.getInstance().getBot().getChannel().messageChannel(String.format("%s, here is a list of my commands: https://bit.ly/2wv1FEt", sender));
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
