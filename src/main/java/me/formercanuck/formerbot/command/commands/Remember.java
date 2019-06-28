package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.timertasks.RememberTask;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.Timer;

public class Remember extends Command {
    @Override
    public String getName() {
        return "remember";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        Bot bot = Main.getInstance().getBot();
        if (channel.isMod(sender) || channel.isWhiteListed(sender)) {
            if (args.length != 0) {
                int delay;
                try {
                    delay = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    channel.messageChannel(String.format("%s, you entered an invalid delay time.", sender));
                    return;
                }

                StringBuilder output = new StringBuilder();

                for (String str : args) {
                    output.append(str).append(" ");
                }

                bot.addRemember(output.toString().substring(args[0].length()));
                channel.messageChannel(String.format("Okay %s, I'll remember that for you.", sender));

                Timer timer = new Timer();
                timer.schedule(new RememberTask(channel), (1000 * 60) * delay);
            } else {
                channel.messageChannel("Usage: !remember <delay> <Message to remember>");
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: Whitelisted+ > !remember <delay> <message> will store a message to broadcast in the specified time (in minutes).";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
