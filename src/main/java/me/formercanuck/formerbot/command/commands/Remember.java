package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.timertasks.RememberTask;
import me.formercanuck.formerbot.twitch.Bot;

import java.util.ArrayList;
import java.util.Timer;

public class Remember extends Command {
    @Override
    public String getName() {
        return "remember";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        if (bot.isMod(sender) || bot.isWhiteListed(sender)) {
            if (args.size() != 0) {
                int delay;
                try {
                    delay = Integer.parseInt(args.get(0));
                } catch (Exception e) {
                    bot.messageChannel(String.format("%s, you entered an invalid delay time.", sender));
                    return;
                }

                args.remove(0);
                StringBuilder output = new StringBuilder();

                for (String str : args) {
                    output.append(str).append(" ");
                }

                bot.addRemember(output.toString());
                bot.messageChannel(String.format("Okay %s, I'll remember that for you.", sender));

                Timer timer = new Timer();
                timer.schedule(new RememberTask(), (1000 * 60) * delay);
            } else {
                bot.messageChannel("Usage: !remember <delay> <Message to remember>");
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
