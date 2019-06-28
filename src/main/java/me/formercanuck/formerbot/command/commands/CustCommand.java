package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;

public class CustCommand extends Command {

    @Override
    public String getName() {
        return "com";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        if (channel.isMod(sender)) {
            if (args.length == 0) {
                channel.messageChannel(String.format("Usage: %scom add | remove | edit.", channel.getChannelFile().get("prefix")));
            } else {
                if (args.length == 1) {
                    switch (args[0]) {
                        case "add":
                            addUsageMessage(channel);
                            break;
                        case "remove":
                            removeUsageMessage(channel);
                            break;
                        case "edit":
                            editUsageMessage(channel);
                            break;
                        default:
                            channel.messageChannel(String.format("Usage: %scom add | remove | edit.", channel.getChannelFile().get("prefix")));
                            break;
                    }
                } else {
                    String name = args[0];
                    if (args.length == 2) {
                        switch (name.toLowerCase()) {
                            case "remove":
                                if (channel.getCommandManager().removeCustomCommand(args[1], channel.getChannelName()))
                                    channel.messageChannel(String.format("%s has removed the command %s.", sender, args[1]));
                                break;
                            case "add":
                                addUsageMessage(channel);
                                break;
                            case "edit":
                                editUsageMessage(channel);
                                break;
                            default:
                                channel.messageChannel(String.format("Usage: %scom add | remove | edit.", channel.getChannelFile().get("prefix")));
                                break;
                        }
                    } else {
                        if (name.equalsIgnoreCase("add") && args.length > 3) {
                            if (args[1].contains("-ul=")) {
                                StringBuilder response = new StringBuilder();
                                for (int i = 3; i < args.length; i++) response.append(args[i]).append(" ");
                                channel.getCommandManager().addCommand(args[2], args[1].substring(4), 0, response.toString().trim());
                                channel.messageChannel(String.format("%s has added the command %s%s with the response %s", sender, channel.getChannelFile().get("prefix"), args[2], response.toString().trim()));
                            } else {
                                StringBuilder response = new StringBuilder();
                                for (int i = 2; i < args.length; i++) response.append(args[i]).append(" ");
                                channel.getCommandManager().addCommand(args[1], "none", 0, response.toString().trim());
                                channel.messageChannel(String.format("%s has added the command %s%s with the response %s", sender, channel.getChannelFile().get("prefix"), args[1], response.toString().trim()));
                            }
                        } else addUsageMessage(channel);
                    }
                }
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return String.format("Usage: %scom add | remove | edit.", channel.getChannelFile().get(" prefix "));
    }

    private void addUsageMessage(Channel channel) {
        channel.messageChannel(String.format("Usage: %scom add (-ul=user level) <name> <response>", channel.getChannelFile().get("prefix")));
    }

    private void removeUsageMessage(Channel channel) {
        channel.messageChannel(String.format("Usage: %scom remove <name>", channel.getChannelFile().get("prefix")));
    }

    private void editUsageMessage(Channel channel) {
        channel.messageChannel(String.format("Usage: %scom edit (-ul=user level) <name> <response>", channel.getChannelFile().get("prefix")));
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
