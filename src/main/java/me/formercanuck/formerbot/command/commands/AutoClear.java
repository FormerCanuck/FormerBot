package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class AutoClear extends Command {
    @Override
    public String getName() {
        return "autoclear";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        if (channel.isMod(sender)) {
            if (args.length == 0) {
                channel.getChannelFile().set("autoClear", !channel.getChannelFile().getBoolean("autoClear"));
                channel.messageChannel(String.format("%s has toggled autoClear current state: " + channel.getChannelFile().getBoolean("autoClear"), sender));
                return;
            } else {
                String command = args[0];

                if (command.equalsIgnoreCase("time"))
                    channel.messageChannel("There is " + channel.getReadTwitchIRC().getTimeRemaining() + " until clearing chat.");
                else
                    try {
                        int time = Integer.parseInt(command);
                        channel.getChannelFile().set("autoClearTime", time);
                        channel.getReadTwitchIRC().cancelClear();
                        channel.messageChannel(String.format("%s has set the auto clear time to %s minutes", sender, time));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !autoClear <cancel | integer> if no argument is specified it will simply toggle the autoClear function.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
