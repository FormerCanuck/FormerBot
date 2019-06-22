package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

public class Leave extends Command {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        Main.getInstance().getBot().getChannel().messageChannel("Okay @" + sender + " o/");
        Main.getInstance().getBot().getChannel().toggleListen(false);
    }

    @Override
    public String getUsage() {
        return "Usage: !leave will make the bot leave the channel. (Currently unless Former is around no way to get the bot back in the channel)";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
