package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;

public class CustCommand extends Command {

    @Override
    public String getName() {
        return "com";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        Bot bot = Main.getInstance().getBot();

        if (bot.getChannel().isMod(sender)) {
            if (args.length == 0) {
                bot.getChannel().messageChannel(String.format("Usage: %scom add | remove | edit.", bot.getBotFile().get("prefix")));
            } else {
                if (args.length == 1) {
                    switch (args[0]) {
                        case "add":
                            addUsageMessage();
                            break;
                        case "remove":
                            removeUsageMessage();
                            break;
                        case "edit":
                            editUsageMessage();
                            break;
                        default:
                            bot.getChannel().messageChannel(String.format("Usage: %scom add | remove | edit.", bot.getBotFile().get("prefix")));
                            break;
                    }
                } else {
                    String name = args[1];
                    if (args.length == 3) {
                        switch (name.toLowerCase()) {
                            case "remove":
                                if (bot.getCommandManager().removeCustomCommand(args[2]))
                                    bot.getChannel().messageChannel(String.format("%s has removed the command %s.", sender, args[2]));
                                break;
                            case "add":
                                addUsageMessage();
                                break;
                            case "edit":
                                editUsageMessage();
                                break;
                            default:
                                bot.getChannel().messageChannel(String.format("Usage: %scom add | remove | edit.", bot.getBotFile().get("prefix")));
                                break;
                        }
                    } else {
                        if (name.equalsIgnoreCase("add") && args.length > 3) {
                            if (args[2].contains("-ul=")) {
                                StringBuilder response = new StringBuilder();
                                for (int i = 4; i < args.length; i++) response.append(args[i]).append(" ");
                                bot.getCommandManager().addCommand(args[3], args[2].substring(4), 0, response.toString().trim());
                                bot.getChannel().messageChannel(String.format("%s has added the command %s%s with the response %s", sender, bot.getBotFile().get("prefix"), args[3], response.toString().trim()));
                            } else {
                                StringBuilder response = new StringBuilder();
                                for (int i = 3; i < args.length; i++) response.append(args[i]).append(" ");
                                bot.getCommandManager().addCommand(args[2], "none", 0, response.toString().trim());
                                bot.getChannel().messageChannel(String.format("%s has added the command %s%s with the response %s", sender, bot.getBotFile().get("prefix"), args[2], response.toString().trim()));
                            }
                        } else addUsageMessage();
                    }
                }
            }
        }
    }

    private void addUsageMessage() {
        Main.getInstance().getBot().getChannel().messageChannel(String.format("Usage: %scom add (-ul=user level) <name> <response>", Main.getInstance().getBot().getBotFile().get("prefix")));
    }

    private void removeUsageMessage() {
        Main.getInstance().getBot().getChannel().messageChannel(String.format("Usage: %scom remove <name>", Main.getInstance().getBot().getBotFile().get("prefix")));
    }

    private void editUsageMessage() {
        Main.getInstance().getBot().getChannel().messageChannel(String.format("Usage: %scom edit (-ul=user level) <name> <response>", Main.getInstance().getBot().getBotFile().get("prefix")));
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}