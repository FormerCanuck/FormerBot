package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class Leave extends Command {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Main.getInstance().getBot().getChannel().messageChannel("Okay @" + sender + " o/");
        System.exit(0);
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
