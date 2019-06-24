package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

public class Listen extends Command {
    @Override
    public String getName() {
        return "listen";
    }

    @Override
    public String getUsage() {
        return "Usage: !listen <user> - Will take users chat and put it in yours using FormerB0t.";
    }

    @Override
    public void onCommand(String sender, String channel, String... args) {
        Bot bot = Main.getInstance().getBot();
        if (!bot.getChannel().isMod(sender)) return;

        if (args.length == 0) bot.getChannel().messageChannel(getUsage());

        bot.getChannel().toggleListen(args[0]);
        bot.getChannel().messageChannel("Joining @" + args[0] + " to listen for messages.");
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
