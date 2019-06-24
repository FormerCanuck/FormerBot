package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class Pals extends Command {
    @Override
    public String getName() {
        return "pals";
    }

    @Override
    public String getUsage() {
        return "Usage: !pals add | remove <user> will add user to pals list.";
    }

    @Override
    public void onCommand(String sender, String channel, String... args) {
        Channel chnl = Main.getInstance().getBot().getChannel();
        if (chnl.isMod(sender)) {
            if (args.length < 2) {
                chnl.messageChannel(getUsage());
            } else {
                if (args[0].equalsIgnoreCase("add")) {
                    if (Main.getInstance().getBot().getBotFile().addPal(args[1]))
                        chnl.messageChannel(String.format("%s has added %s to the list of pals.", sender, args[1]));
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (Main.getInstance().getBot().getBotFile().removePal(args[1]))
                        chnl.messageChannel(String.format("%s has removed %s from the list of pals.", sender, args[1]));
                } else
                    chnl.messageChannel(getUsage());
            }
        }
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
