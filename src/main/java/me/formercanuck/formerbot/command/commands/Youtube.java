package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.GetJsonData;
import me.formercanuck.formerbot.utils.MiscUtils;

public class Youtube extends Command {
    @Override
    public String getName() {
        return "youtube";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {//https://www.youtube.com/channel/
        if (GetJsonData.getInstance().getIDFromYoutube(channel.getChannelName()) == null) {
            channel.messageChannel(sender + ", " + channel.getChannelName() + " does not currently have a youtube.");
            return;
        }

        int days = Math.toIntExact(GetJsonData.getInstance().daysSinceLastUpload(channel.getChannelName()));

        channel.messageChannel(String.format("%s, check out %s at https://www.youtube.com/channel/%s their last upload was %s which was posted %s days ago and you can find it here: %s", sender, channel, GetJsonData.getInstance().getIDFromYoutube(channel.getChannelName()), GetJsonData.getInstance().getLastVideoTitle(channel.getChannelName()), MiscUtils.getDateString(days), GetJsonData.getInstance().getLastVideoLink(channel.getChannelName())));
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !youtube, Displays the last upload for the current channel and provides a link.";
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
