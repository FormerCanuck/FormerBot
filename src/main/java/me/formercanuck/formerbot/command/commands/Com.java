package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class Com extends Command {
    @Override
    public String getName() {
        return "com";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        if (bot.isMod(sender)) {
            if (args.size() == 0) bot.messageChannel("Useage: !com add | remove");
            if (args.get(0).equalsIgnoreCase("add")) {
                if (args.size() < 5) bot.messageChannel("Useage: !com add name cooldown response");
                String command = args.get(1);
                int cooldown = Integer.parseInt(args.get(2));

                StringBuilder str = new StringBuilder();

                for (int i = 3; i < args.size(); i++) {
                    str.append(args.get(i)).append(" ");
                }

                CustomCommand temp = new CustomCommand(command, cooldown, str.toString().trim());
                if (bot.getCommandManager().getCustomCommand(temp.getName()) == null) {
                    bot.getCommandManager().addCustomCommand(temp);
                    bot.messageChannel(String.format("%s added the command !%s with a cooldown time of %s minutes, and the response is %s", sender, command, cooldown, str.toString().trim()));
                }
            } else if (args.get(0).equalsIgnoreCase("rem")) {
                if (args.size() < 2) bot.messageChannel("Useage: !com rem name");
                if (bot.getCommandManager().getCustomCommand(args.get(1)) != null) {
                    bot.getCommandManager().removeCustomCommand(args.get(1));
                    bot.messageChannel(String.format("%s removed command %s", sender, args.get(1)));
                } else bot.messageChannel("NULL");
            }
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
