package me.formercanuck.formerbot.timertasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.TimerTask;

public class Followers extends TimerTask {

    private Channel channel;

    private int lastRan = 0;

    private boolean hasRun = false;

    public Followers(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        if (!hasRun)
            loadFollows();
        checkPals();
    }

    private void checkPals() {
        JsonArray array = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + channel.getChannelName()).getAsJsonObject().get("data").getAsJsonArray();

        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            if (obj.get("type") != null) {
                if (!channel.isLive())
                    channel.setLive(true);
            } else channel.setLive(false);

            String id = obj.get("game_id").getAsString();

            StringBuilder str = new StringBuilder();
            int online = 0;
            for (String s : channel.getChannelFile().getPals()) {
                JsonObject temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + s).getAsJsonObject();
                JsonArray tempArray = temp.get("data").getAsJsonArray();

                if (tempArray.size() > 0 && tempArray.get(0).getAsJsonObject().get("game_id").getAsString().equalsIgnoreCase(id)) {
                    str.append("/").append(s.replace("@", " ").trim());
                    online++;
                }
            }

            if (online > 0) {
                if (lastRan == 0) {
                    channel.messageChannel(String.format("Check out everyone's perspective at: https://multistre.am/%s%s/layout4/", channel.getChannelName(), str.toString().trim()));
                } else if (lastRan == 3) {
                    lastRan = 0;
                }
                lastRan++;
            }
        }
    }

    private void loadFollows() {
        JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + channel.getChannelID() + "&first=100");

        Main.getInstance().getConsole().println("[Bot]: loading followers...");

        while (temp.getAsJsonObject().get("pagination").getAsJsonObject().has("cursor")) {
            JsonElement follows = temp.getAsJsonObject().get("data");

            for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
                String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString().replace("\"", " ").trim();
                String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString().replace("\"", " ").trim();
                channel.addFollower(user, followDate.substring(0, 10));
            }

            temp =
                    GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + channel.getChannelID() + "&first=100&after=" + temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").getAsString().replace("\"", " ").trim());
            Main.getInstance().getConsole().error(channel.followers.size() + " followers loaded");
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        channel.getChannelFile().set("follows", channel.followers);
        Main.getInstance().getConsole().println("[Bot]: finished loading followers...");
    }
}
