package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

public class AutoClear extends Command {
    @Override
    public String getName() {
        return "autoclear";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        Bot bot = Main.getInstance().getBot();
        if (bot.getChannel().isMod(sender)) {
            if (args.length == 0) {
                bot.getBotFile().set("autoClear", !bot.getBotFile().getBoolean("autoClear"));
                bot.getChannel().messageChannel(String.format("%s has toggled autoClear current state: " + bot.getBotFile().getBoolean("autoClear"), sender));
                return;
            } else {
                String command = args[0];

                if (command.equalsIgnoreCase("time"))
                    bot.getChannel().messageChannel("There is " + bot.getReadTwitchIRC().getTimeRemaining() + " until clearing chat.");
                else
                    try {
                        int time = Integer.parseInt(command);
                        bot.getBotFile().set("autoClearTime", time);
                        bot.getReadTwitchIRC().cancelClear();
                        bot.getChannel().messageChannel(String.format("%s has set the auto clear time to %s minutes", sender, time));
                    } catch (Exception e) {
                        System.out.println(e.getCause());
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public String getUsage() {
        return "Usage: !autoClear <cancel | integer> if no argument is specified it will simply toggle the autoClear function.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
