package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.utils.SortArrayList;

import java.util.ArrayList;

public class Help extends Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        Bot bot = Main.getInstance().getBot();
        StringBuilder str = new StringBuilder();

        ArrayList<String> commands = new ArrayList<>();

        for (Command cmd : bot.getCommandManager().getCommands()) commands.add(cmd.getName());

        SortArrayList list = new SortArrayList(commands);
        list.sortAscending();

        if (args.length == 0) {
            for (String s : list.getArrayList()) {
                str.append(", ").append(s);
            }
            bot.getChannel().messageChannel(String.format("%s, here is a list of my commands (%s): %s", sender, bot.getCommandManager().getCommands().size(), str.toString().substring(2)));
        } else if (args.length == 1) {
            String commmand = args[0];
            for (Command cmd : Main.getInstance().getBot().getCommandManager().getCommands()) {
                if (cmd.getName().equalsIgnoreCase(commmand)) {
                    Main.getInstance().getBot().getChannel().messageChannel(cmd.getUsage());
                }
            }
        }
    }

    @Override
    public String getUsage() {
        return "help (command), if blank it will list all commands.";
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
