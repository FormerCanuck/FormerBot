package me.formercanuck.formerbot.command;

import me.formercanuck.formerbot.twitch.Channel;

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

    public void onCommand(String sender, Channel channel, String[] args) {
        if (canUse(sender, channel))
            channel.messageChannel(response.replaceAll("%user%", "@" + args[0]));
    }

    private boolean canUse(String sender, Channel channel) {
        switch (userLevel) {
            case "mod":
                return channel.isMod(sender);
            case "whitelist":
                return channel.isWhiteListed(sender) || channel.isMod(sender);
            case "follow":
                return channel.isFollowing(sender) || channel.isWhiteListed(sender) || channel.isMod(sender);
            default:
                return true;
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !" + name;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }
}
