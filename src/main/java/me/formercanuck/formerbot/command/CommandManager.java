package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.commands.TopClips;
import me.formercanuck.formerbot.command.commands.Uptime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    public List<Command> commandList;

    public CommandManager() {
        commandList = new ArrayList<>();
        commandList.add(new TopClips());
        commandList.add(new Uptime());
    }

    public void onCommand(String sender, String channel, String command, String[] args) {
        for (Command cmd : commandList) {
            if (cmd.getName().equalsIgnoreCase(command)) {
                cmd.onCommand(sender, channel, args);
            }
        }

        Main.getInstance().getConsole().println(command);
        Main.getInstance().getConsole().println(Arrays.toString(args));
    }
}
