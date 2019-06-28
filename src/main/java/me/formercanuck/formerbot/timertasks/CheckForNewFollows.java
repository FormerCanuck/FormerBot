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

        System.out.println(channel.getChannelName());
//        JsonArray array = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + channel.getChannelName()).getAsJsonObject().get("data").getAsJsonArray();
//
//        if (array.size() > 0) {
//            JsonObject obj = array.get(0).getAsJsonObject();
//            if (obj.get("type") != null) {
//                if (!channel.isLive())
//                    channel.setLive(true);
//            } else channel.setLive(false);
//
//            String id = obj.get("game_id").getAsString();
//
//            StringBuilder str = new StringBuilder();
//            int online = 0;
//            for (String s : channel.getChannelFile().getPals()) {
//                JsonObject temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + s).getAsJsonObject();
//                JsonArray tempArray = temp.get("data").getAsJsonArray();
//
//                if (tempArray.size() > 0 && tempArray.get(0).getAsJsonObject().get("game_id").getAsString().equalsIgnoreCase(id)) {
//                    str.append("/").append(s.replace("@", " ").trim());
//                    online++;
//                }
//            }
//            if (online > 0)
//                channel.messageChannel(String.format("Check out everyone's perspective at: https://multistre.am/%s%s/layout4/", channel.getChannelName(), str.toString().trim()));
//        }
    }
}
