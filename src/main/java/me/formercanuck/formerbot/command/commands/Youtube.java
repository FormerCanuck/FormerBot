package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.utils.GetJsonData;

public class Youtube extends Command {
    @Override
    public String getName() {
        return "youtube";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {//https://www.youtube.com/channel/
        channel = channel.substring(1);
        if (GetJsonData.getInstance().getIDFromYoutube(channel) == null) {
            Main.getInstance().getBot().getChannel().messageChannel(sender + ", " + channel + " does not currently have a youtube.");
            return;
        }

        int days = Math.toIntExact(GetJsonData.getInstance().daysSinceLastUpload(channel));

        int year = (days / 365);
        days = days % 365;
        int week = days / 7;
        days = days % 7;

        StringBuilder str = new StringBuilder();

        if (year > 0) str.append(year).append(" years, ");

        if (week > 0) str.append(week).append(" weeks, and ");

        if (days > 0) str.append(days).append(" days");

        Main.getInstance().getBot().getChannel().messageChannel(String.format("%s, check out %s at https://www.youtube.com/channel/%s their last upload was %s which was posted %s days ago and you can find it here: %s", sender, channel, GetJsonData.getInstance().getIDFromYoutube(channel), GetJsonData.getInstance().getLastVideoTitle(channel), str.toString().trim(), GetJsonData.getInstance().getLastVideoLink(channel)));
    }

    @Override
    public String getUsage() {
        return "Usage: !youtube, Displays the last upload for the current channel and provides a link.";
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
