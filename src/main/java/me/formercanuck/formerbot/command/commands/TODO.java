package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.TodoFile;

import java.util.ArrayList;

public class TODO extends Command {

    @Override
    public String getName() {
        return "todo";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        TodoFile todoFile = new TodoFile();

        if (bot.isMod(sender) || bot.isWhiteListed(sender)) {
            StringBuilder str = new StringBuilder();

            for (String s : args) {
                str.append(s).append(" ");
            }

            todoFile.addTODO(str.toString());

            bot.messageChannel(String.format("%s has added: %s to the todo list.", sender, str.toString()));
        }
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
