package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class Followage extends Command {
    @Override
    public String getName() {
        return "followage";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        if (Main.getInstance().getBot().isFollowing(sender.toLowerCase())) {
            Main.getInstance().getBot().messageChannel(String.format("%s, you have been following since: %s", sender, Main.getInstance().getBot().getFollowDate(sender)));
        } else {
            Main.getInstance().getBot().messageChannel(String.format("%s, you are not following.", sender));

            System.out.println(Main.getInstance().getBot().getFollowers().keySet());
        }
    }
}
