package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.TodoFile;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;

public class TODO extends Command {

    @Override
    public String getName() {
        return "todo";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        Bot bot = Main.getInstance().getBot();
        TodoFile todoFile = new TodoFile();

        if (channel.isMod(sender) || channel.isWhiteListed(sender)) {
            StringBuilder str = new StringBuilder();

            for (String s : args) {
                str.append(s).append(" ");
            }

            todoFile.addTODO(str.toString());

            channel.messageChannel(String.format("%s has added: %s to the todo list.", sender, str.toString()));
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: Whitelist+ > !todo <todo task> Gives Former more work.";
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
