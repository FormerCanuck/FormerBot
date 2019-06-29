package me.formercanuck.formerbot.timertasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.GetJsonData;
import me.formercanuck.formerbot.utils.MiscUtils;
import me.formercanuck.formerbot.utils.SortArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class UpdateViewers extends TimerTask {

    private Channel channel;
    private ConfigFile channelFile;
    private HashMap<String, Integer> points;

    private int increment;

    private int lastRun = 0;

    public UpdateViewers(Channel channel) {
        this.channel = channel;
        this.channelFile = channel.getChannelFile();
        if (channelFile.contains("pointsPerMinute")) increment = channelFile.getInt("pointsPerMinute");
        else {
            increment = 1;
            channelFile.set("pointsPerMinute", increment);
        }
        getPoints();
    }

    private JsonElement getViewerList(Channel channel) {
        return GetJsonData.getInstance().getJson(String.format("https://tmi.twitch.tv/group/user/%s/chatters", channel.getChannelName()));
    }

    private void getPoints() {
        if (channelFile.contains("points")) points = (HashMap<String, Integer>) channelFile.get("points");
        else points = new HashMap<>();
    }

    private void checkNewFollows() {
        JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + channel.getChannelID() + "&first=100");

        JsonElement follows = temp.getAsJsonObject().get("data");

        for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
            String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString().replace("\"", " ").trim();
            String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString().replace("\"", " ").trim();
            channel.addFollower(user, followDate.substring(0, 10));
        }

        HashMap<String, String> followFile = (HashMap<String, String>) channel.getChannelFile().get("follows");

        ArrayList<String> newFollows = new ArrayList<>();

        for (String s : channel.followers.keySet()) {
            if (!followFile.containsKey(s.toLowerCase())) {
                newFollows.add(s.toLowerCase());
            }
        }

        System.out.println(newFollows.iterator().next());

        if (newFollows.size() > 0) {
            SortArrayList list = new SortArrayList(newFollows);
            list.sortAscending();

            StringBuilder str = new StringBuilder();
            for (String s : list.getArrayList()) {
                str.append(", ").append(s);
            }

            channel.messageChannel(String.format("Thank you %s for the follow.", str.toString()));
            newFollows.clear();
            channel.getChannelFile().set("follows", channel.followers);
        }
    }

    @Override
    public void run() {
        checkNewFollows();
        if (lastRun == 0) {
            JsonObject chatters = getViewerList(channel).getAsJsonObject().get("chatters").getAsJsonObject();

            List<String> viewers = new ArrayList<>();

            JsonArray vip = chatters.get("vips").getAsJsonArray();
            JsonArray moderators = chatters.get("moderators").getAsJsonArray();
            JsonArray staff = chatters.get("staff").getAsJsonArray();
            JsonArray admins = chatters.get("admins").getAsJsonArray();
            JsonArray global_mods = chatters.get("global_mods").getAsJsonArray();
            JsonArray views = chatters.get("viewers").getAsJsonArray();

            for (JsonElement element : vip) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : moderators) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : staff) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : admins) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : global_mods) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : views) {
                viewers.add(MiscUtils.strip(element.toString()));
            }
            channel.setViewers(viewers);
            lastRun = 0;
        } else lastRun++;

        if (lastRun == 5) lastRun = 0;

        if (channelFile.contains("points")) points = (HashMap<String, Integer>) channelFile.get("points");
        else points = new HashMap<>();

        for (String s : channel.getViewers()) {
            s = s.toLowerCase();
            if (channel.isFollowing(s)) {
                if (points.containsKey(s)) {
                    points.put(s, points.get(s) + increment);
                } else {
                    points.put(s, increment);
                }
            }
        }
        channelFile.set("points", points);
    }
}
