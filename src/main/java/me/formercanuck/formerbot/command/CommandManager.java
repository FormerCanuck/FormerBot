package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.commands.*;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager {

    public List<String> cooldown = new ArrayList<>();
    private List<Command> commandList;
    private List<String> disableCommands = new ArrayList<>();

    private HashMap<String, ArrayList<String>> customCommands = new HashMap<>();

    private Channel channel;

    public CommandManager(Channel channel) {
        this.channel = channel;
        commandList = new ArrayList<>();

        if (channel.getChannelFile() == null) System.exit(0);

        if (channel.getChannelFile().contains("disabledCommands"))
            disableCommands = (List<String>) channel.getChannelFile().get("disabledCommands");

        commandList.add(new TopClips());
        commandList.add(new Uptime());
        commandList.add(new Pals());
        commandList.add(new Prefix());
        commandList.add(new ServerAge());
        commandList.add(new Points());
        commandList.add(new Duel());
        commandList.add(new Accept());
        commandList.add(new Youtube());
        commandList.add(new FollowAge());
//        commandList.add(new Listen());
        commandList.add(new Whitelist());
        commandList.add(new Watchlist());
        commandList.add(new Link());
        commandList.add(new TODO());
        commandList.add(new Remember());
        commandList.add(new Help());
        commandList.add(new AutoClear());
        commandList.add(new Leave());
        commandList.add(new Join());
        commandList.add(new MultiStream());
        commandList.add(new Disable());
        commandList.add(new Enable());
        commandList.add(new CustCommand());

        loadCustomCommands();
    }

    private void loadCustomCommands() {
        if (channel.getChannelFile().contains("commands")) {
            customCommands = (HashMap<String, ArrayList<String>>) channel.getChannelFile().get("commands");

            for (String name : customCommands.keySet()) {
                ArrayList<String> args = customCommands.get(name);
                commandList.add(new CustomCommand(name, args.get(0), Integer.parseInt(args.get(1)), args.get(2)));
            }
        }
    }

    public void addCommand(String name, String userLevel, int cooldown, String response) {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(userLevel);
        temp.add(String.valueOf(cooldown));
        temp.add(response);
        customCommands.put(name, temp);
        commandList.add(new CustomCommand(name, userLevel, cooldown, response));
        channel.getChannelFile().set("commands", customCommands);
    }

    public boolean removeCustomCommand(String name, String channel) {
        if (customCommands.containsKey(name)) {
            commandList.remove(getCommand(name));
            customCommands.remove(name);
            Main.getInstance().getBot().getChannel(channel).getChannelFile().set("commands", customCommands);
            return true;
        }
        return false;
    }

    void addCooldown(String commandName) {
        cooldown.add(commandName);
    }

    public void onCommand(String sender, Channel channel, String command, String[] args) {
        for (Command cmd : commandList) {
            if (!cooldown.contains(cmd.getName()) && !disableCommands.contains(cmd.getName()) && channel.getShouldListen()) {
                if (cmd.getName().equalsIgnoreCase(command)) {
                    cmd.onCommand(sender, channel, args);
                    cmd.cooldown(channel);
                    break;
                }
            } else if (cmd.getName().equalsIgnoreCase("join")) {
                cmd.onCommand(sender, channel, args);
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

    public boolean disableCommand(String command, Channel channel) {
        if (getCommand(command) != null) {
            disableCommands.add(command);
            channel.getChannelFile().set("disabledCommands", disableCommands);
            return true;
        } else return false;
    }

    public boolean enableCommand(String command, Channel channel) {
        if (getCommand(command) != null) {
            disableCommands.remove(command);
            channel.getChannelFile().set("disabledCommands", disableCommands);
            return true;
        } else return false;
    }

    public List<Command> getCommands() {
        return commandList;
    }
}
