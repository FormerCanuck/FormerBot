package me.formercanuck.formerbot.command;

import java.util.ArrayList;

public abstract class Command {

    public abstract String getName();

    public abstract void onCommand(String sender, String channel, ArrayList<String> args);
}
