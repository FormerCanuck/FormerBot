package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.commands.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public List<String> cooldown = new ArrayList<>();
    private List<Command> commandList;

    private List<String> disableCommands = new ArrayList<>();

    public CommandManager() {
        commandList = new ArrayList<>();

        if (Main.getInstance().getBot().getBotFile().contains("disabledCommands"))
            disableCommands = (List<String>) Main.getInstance().getBot().getBotFile().get("disabledCommands");

        commandList.add(new TopClips());
        commandList.add(new Uptime());
        commandList.add(new ServerAge());
        commandList.add(new Youtube());
        commandList.add(new FollowAge());
        commandList.add(new Whitelist());
        commandList.add(new Watchlist());
        commandList.add(new Link());
        commandList.add(new TODO());
        commandList.add(new Remember());
        commandList.add(new Help());
        commandList.add(new AutoClear());
        commandList.add(new Leave());
        commandList.add(new Disable());
    }

    void addCooldown(String commandName) {
        cooldown.add(commandName);
    }

    public void onCommand(String sender, String channel, String command, ArrayList<String> args) {
        for (Command cmd : commandList) {
            if (!cooldown.contains(cmd.getName()) && !disableCommands.contains(cmd.getName()))
                if (cmd.getName().equalsIgnoreCase(command)) {
                    cmd.onCommand(sender, channel, args);
                    cmd.cooldown();
                    break;
                }
        }
    }

    public Command getCommand(String command) {
        for (Command cmd : commandList) {
            if (cmd.getName().equalsIgnoreCase(command)) return cmd;
        }
        return null;
    }

    public boolean disableCommand(String command) {
        if (getCommand(command) != null) {
            disableCommands.add(command);
            return true;
        } else return false;
    }
}
