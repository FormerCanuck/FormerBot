package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.command.commands.Followage;
import me.formercanuck.formerbot.command.commands.Help;
import me.formercanuck.formerbot.command.commands.TopClips;
import me.formercanuck.formerbot.command.commands.Uptime;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public List<Command> commandList;

//    public ConfigFile commandFile;

    public CommandManager() {
        commandList = new ArrayList<>();
        commandList.add(new TopClips());
        commandList.add(new Uptime());

        commandList.add(new Followage());
        commandList.add(new Help());
    }

    public void onCommand(String sender, String channel, String command, ArrayList<String> args) {
        for (Command cmd : commandList) {
            if (cmd.getName().equalsIgnoreCase(command)) {
                cmd.onCommand(sender, channel, args);
            }
        }
    }
}
