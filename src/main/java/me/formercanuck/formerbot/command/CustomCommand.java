package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.Main;

public class CustomCommand extends Command {

    private String name;
    private String response;
    private String userLevel;

    private int cooldown;

    public CustomCommand(String name, String userLevel, int cooldown, String response) {
        this.name = name;
        this.response = response;
        this.userLevel = userLevel;
        this.cooldown = cooldown;
    }

    @Override
    public String getName() {
        return name;
    }

    public void onCommand(String sender, String channel, String[] args) {
        if (canUse(sender))
            Main.getInstance().getBot().getChannel().messageChannel(response);
    }

    private boolean canUse(String sender) {
        switch (userLevel) {
            case "mod":
                return Main.getInstance().getBot().getChannel().isMod(sender);
            case "whitelist":
                return Main.getInstance().getBot().getChannel().isWhiteListed(sender) || Main.getInstance().getBot().getChannel().isMod(sender);
            case "follow":
                return Main.getInstance().getBot().getChannel().isFollowing(sender) || Main.getInstance().getBot().getChannel().isWhiteListed(sender) || Main.getInstance().getBot().getChannel().isMod(sender);
            default:
                return true;
        }
    }

    @Override
    public String getUsage() {
        return "Usage: !" + name;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }
}
