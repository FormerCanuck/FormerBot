package me.formercanuck.formerbot.utils;

import com.google.gson.JsonElement;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.TimerTask;

public class Followers extends TimerTask {

    private Channel channel;

    public Followers(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        loadFollows(channel);
    }

    public void loadFollows(Channel channel) {
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
