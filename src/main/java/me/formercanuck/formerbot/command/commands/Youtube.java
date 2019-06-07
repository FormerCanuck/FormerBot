package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.ArrayList;

public class Youtube extends Command {
    @Override
    public String getName() {
        return "youtube";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {//https://www.youtube.com/channel/
        channel = channel.substring(1);
        if (GetJsonData.getInstance().getIDFromYoutube(channel) == null) {
            Main.getInstance().getBot().messageChannel(sender + ", " + channel + " does not currently have a youtube.");
            return;
        }
        Main.getInstance().getBot().messageChannel(String.format("%s, check out %s at https://www.youtube.com/channel/%s their last upload was %s which was posted %s days ago and you can find it here: %s", sender, channel, GetJsonData.getInstance().getIDFromYoutube(channel), GetJsonData.getInstance().getLastVideoTitle(channel), GetJsonData.getInstance().daysSinceLastUpload(channel), GetJsonData.getInstance().getLastVideoLink(channel)));
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
