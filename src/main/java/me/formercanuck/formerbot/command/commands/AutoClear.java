package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class AutoClear extends Command {
    @Override
    public String getName() {
        return "autoclear";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        if (bot.isMod(sender)) {
            if (args.size() == 0) {
                bot.getBotFile().set("autoClear", !bot.getBotFile().getBoolean("autoClear"));
                bot.messageChannel(String.format("%s has toggled autoClear current state: " + bot.getBotFile().getBoolean("autoClear"), sender));
                return;
            } else {
                String command = args.get(0);

                if (command.equalsIgnoreCase("help"))
                    bot.messageChannel("Usage: !autoClear <cancel | integer> if no argument is specified it will simply toggle the autoClear function.");
                else if (command.equalsIgnoreCase("time"))
                    bot.messageChannel("There is " + bot.getReadTwitchIRC().getTimeRemaining() + " until clearing chat.");
                else
                    try {
                        int time = Integer.parseInt(command);
                        bot.getBotFile().set("autoClearTime", time);
                        bot.messageChannel(String.format("%s has set the auto clear time to %s minutes", sender, time));
                    } catch (Exception e) {
                        System.out.println(e.getCause());
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
