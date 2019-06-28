package me.formercanuck.formerbot.timertasks;

import com.google.gson.JsonElement;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.TimerTask;

public class CheckForNewFollows extends TimerTask {

    private Channel channel;

    public CheckForNewFollows(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?first=1&to_id=" + channel.getChannelID());

        String lastFollow = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("from_name").getAsString();
        if (channel.getLastFollow().isEmpty()) channel.setLastFollow(lastFollow);
        else {
            if (channel.isHasCheckedFirstFollow())
                if (!lastFollow.equalsIgnoreCase(channel.getLastFollow())) {
                    channel.setLastFollow(lastFollow);
                    channel.messageChannel(String.format("Thanks for the follow @%s!", lastFollow));
                } else System.out.println("No new follows");
            else channel.setHasCheckedFirstFollow();
        }
    }
}
