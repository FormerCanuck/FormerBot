package me.formercanuck.formerbot.command;

public abstract class Command {

    public abstract String getName();

    public abstract void onCommand(String sender, String channel, String[] args);
}
