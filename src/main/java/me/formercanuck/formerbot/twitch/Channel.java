package me.formercanuck.formerbot.twitch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channel {

    private String channel;

    private String channelID;

    private Bot bot;

    private ArrayList<String> mods = new ArrayList<>();
    private ArrayList<String> whitelisted = new ArrayList<>();
    private ArrayList<String> watchlist;

    private HashMap<String, String> followers = new HashMap<>();

    public Channel(String channel) {
        this.channel = String.format("#%s", channel);
        this.bot = Main.getInstance().getBot();

        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users?login=" + getChannelName());

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();
            channelID = obj.get("id").getAsString();
        }
        join();
    }

    private void join() {
        bot.sendRawMessage(String.format("JOIN %s", channel));
    }

    public void messageChannel(String message) {
        bot.sendRawMessage(String.format("PRIVMSG %s :/me %s", getChannel(), message));
    }

    public String getChannel() {
        return channel;
    }

    public String getChannelName() {
        return channel.substring(1);
    }

    public String getChannelID() {
        return channelID;
    }

    public void addMod(String user) {
        mods.add(user);
    }

    public boolean isMod(String user) {
        return mods.contains(user.toLowerCase());
    }

    private void addFollower(String user, String followDate) {
        followers.put(user.toLowerCase(), followDate);
    }

    public String getFollowDate(String user) {
        return followers.get(user.toLowerCase());
    }

    public boolean isFollowing(String user) {
        for (String key : followers.keySet()) {
            if (key.equalsIgnoreCase(user)) return true;
        }
        return false;
    }

    public ArrayList<String> getWhitelisted() {
        return whitelisted;
    }

    public void setWhitelisted(ArrayList<String> watchlisted) {
        this.whitelisted = watchlisted;
    }

    public boolean isWhiteListed(String sender) {
        return getWhitelisted().contains(sender.toLowerCase());
    }

    public boolean onWatchlist(String sender) {
        return watchlist.contains(sender.toLowerCase());
    }

    public List<String> getWatchList() {
        return watchlist;
    }

    public void setWatchList(ArrayList<String> watchlist) {
        this.watchlist = watchlist;
    }

    public void loadFollows() {
        JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + getChannelID() + "&first=100");

        Main.getInstance().getConsole().println("[Bot]: loading followers...");

        while (temp.getAsJsonObject().get("pagination").getAsJsonObject().has("cursor")) {
            JsonElement follows = temp.getAsJsonObject().get("data");

            for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
                String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString().replace("\"", " ").trim();
                String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString().replace("\"", " ").trim();
                addFollower(user, followDate.substring(0, 10));
            }

            temp =
                    GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + getChannelID() + "&first=100&after=" + temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").getAsString().replace("\"", " ").trim());
            Main.getInstance().getConsole().error(followers.size() + " followers loaded");
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Main.getInstance().getConsole().println("[Bot]: finished loading followers...");
    }
}