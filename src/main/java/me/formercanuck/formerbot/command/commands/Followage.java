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
        if (args.size() == 0) {
            getFollowage(sender, channel);
        } else if (args.size() == 1 && Main.getInstance().getBot().isMod(sender)) {
            getFollowage(args.get(0), channel);
        }
    }

    public void getFollowage(String user, String channel) {
        if (Main.getInstance().getBot().isFollowing(user.toLowerCase())) {
            Main.getInstance().getBot().messageChannel(String.format("%s has been following @%s since %s", user, channel.substring(1), Main.getInstance().getBot().getFollowDate(user)));
        } else {
            Main.getInstance().getBot().messageChannel(String.format("%s, you are not following.", user));
        }
    }
}
