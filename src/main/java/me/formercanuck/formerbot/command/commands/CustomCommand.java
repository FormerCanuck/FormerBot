package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.util.ArrayList;

public class CustomCommand extends Command {

    private String name;
    private String response;

    private int cooldown;

    public CustomCommand(String name, int cooldown, String response) {
        this.name = name;
        this.response = response;
        this.cooldown = cooldown;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Main.getInstance().getBot().messageChannel(response);
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    public String getResponse() {
        return response;
    }
}
