package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.command.commands.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public List<String> cooldown = new ArrayList<>();
    private List<Command> commandList;

//    public ConfigFile commandFile;

    public CommandManager() {
        commandList = new ArrayList<>();
        commandList.add(new TopClips());
        commandList.add(new Uptime());
        commandList.add(new ServerAge());
        commandList.add(new Youtube());
        commandList.add(new FollowAge());
        commandList.add(new Whitelist());
        commandList.add(new Link());
        commandList.add(new Remember());
        commandList.add(new Help());
        commandList.add(new AutoClear());
    }

    void addCooldown(String commandName) {
        cooldown.add(commandName);
    }

    public void onCommand(String sender, String channel, String command, ArrayList<String> args) {
        for (Command cmd : commandList) {
            if (!cooldown.contains(cmd.getName()))
                if (cmd.getName().equalsIgnoreCase(command)) {
                    cmd.onCommand(sender, channel, args);
                    cmd.cooldown();
                }
        }
    }
}
