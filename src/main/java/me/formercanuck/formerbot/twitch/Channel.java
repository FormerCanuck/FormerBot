package me.formercanuck.formerbot.twitch;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.ChannelConsole;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.timertasks.DuelTask;
import me.formercanuck.formerbot.timertasks.Update;
import me.formercanuck.formerbot.utils.GetJsonData;
import me.formercanuck.formerbot.utils.MiscUtils;
import me.formercanuck.formerbot.utils.SortArrayList;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class Channel {

    private String channel;

    private String channelID;

    private HashMap<String, String> followers = new HashMap<>();

    private Bot bot;
    private CommandManager commandManager;
    private ReadTwitchIRC readTwitchIRC;

    private boolean shouldListen = true;
    private boolean isLive;

    private ArrayList<String> mods = new ArrayList<>();
    private ArrayList<String> whitelisted = new ArrayList<>();
    private ArrayList<String> watchlist;
    private ConfigFile channelFile;
    private HashMap<String, Integer> points;

    private List<String> hasChatted;
    private List<String> viewers;

    private ChannelConsole console;

    private List<DuelTask> duels = new ArrayList<>();

    private Timer update;

    private int minutesPassed = -1;
    private int increment;
    private boolean followsLoaded = false;
    private boolean isLoading = false;

    public void leaveChannel() {
        // TODO: Implement saving of important things.
        bot.sendRawMessage("PART " + getChannel());
    }

    public List<DuelTask> getDuels() {
        return duels;
    }

    public DuelTask getDuel(String user) {
        for (DuelTask task : getDuels()) {
            if (task.getID().equalsIgnoreCase(user)) return task;
        }
        return null;
    }

    public void addDuel(DuelTask duelTask) {
        getDuels().add(duelTask);
    }

    private List<String> getViewers() {
        return viewers;
    }

    private void setViewers(List<String> viewers) {
        this.viewers = viewers;
    }

    public HashMap<String, Integer> getPointsMap() {
        this.points = channelFile.getPointsMap();
        return this.points;
    }

    private void savePoints() {
        channelFile.set("points", points);
    }

    private void setPoints(String user, int points) {
        getPointsMap().put(user, points);
        savePoints();
    }

    public int getPoints(String user) {
        return getPointsMap().get(user.toLowerCase());
    }

    public void addPoints(String user, int points) {
        user = user.toLowerCase();
        setPoints(user, getPointsMap().get(user) + points);
    }

    public void removePoints(String user, int points) {
        user = user.toLowerCase();
        if (getPoints(user) > points) {
            setPoints(user, getPointsMap().get(user) - points);
        }
    }

    public ChannelConsole getConsole() {
        return console;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public List<String> getHasChatted() {
        return hasChatted;
    }

    public void addChatted(String user) {
        hasChatted.add(user);
    }

    private void goLive() {
        hasChatted = new ArrayList<>();
    }

    public boolean isLive() {
        return isLive;
    }

    private void setLive(boolean isLive) {
        if (isLive) {
            goLive();
            this.isLive = true;
        } else {
            this.isLive = false;
        }
    }

    void join() {
        bot.sendRawMessage(String.format("JOIN %s", channel));
    }

    public void messageChannel(String message) {
        bot.sendRawMessage(String.format("PRIVMSG %s :/me %s", getChannel(), message));
        console.println(String.format("PRIVMSG %s :/me %s", getChannel(), message), Color.ORANGE);
    }

    private int timesRan = 0;

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
        user = user.toLowerCase();
        HashMap<String, String> follows = channelFile.getHashMap("follows");
        if (follows.containsKey(user)) {
            return follows.get(user);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(new Date());
        }
    }

    public boolean isFollowing(String user) {
        HashMap<String, String> follows;
        if (channelFile.contains("follows"))
            follows = channelFile.getHashMap("follows");
        else return false;

        for (String key : follows.keySet()) {
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

    public void toggleListen(boolean b) {
        shouldListen = b;
    }

    public boolean getShouldListen() {
        return shouldListen;
    }

    public ConfigFile getChannelFile() {
        return this.channelFile;
    }

    public ReadTwitchIRC getReadTwitchIRC() {
        return readTwitchIRC;
    }

    public Channel(String channel) {
        this.viewers = new ArrayList<>();
        this.channel = String.format("#%s", channel);
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users?login=" + getChannelName());

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();
            channelID = obj.get("id").getAsString();
        }
        this.bot = Main.getInstance().getBot();

        channelFile = new ConfigFile(getChannelName().toLowerCase());

        configDefaults();

        this.commandManager = new CommandManager(this);

        hasChatted = new ArrayList<>();

        this.console = new ChannelConsole(channel, this);
        readTwitchIRC = new ReadTwitchIRC(bot.getTwitchConnection(), this);
        new Thread(readTwitchIRC).start();

        update = new Timer();
        update.scheduleAtFixedRate(new Update(this), 0, 1000 * 60);

        increment = channelFile.getInt("pointsPerMinute");
    }

    public void whisper(String user, String message) {
        user = user.toLowerCase();
        bot.sendRawMessage(String.format("PRIVMSG #jtv :/w %s %s", user, message));
//        bot.sendRawMessage(String.format("PRIVMSG %s :/w %s %s", user, getChannel(), message));
        console.println(String.format("PRIVMSG #jtv :/w %s %s", user, message), Color.ORANGE);
    }

    public void update() {
        checkIfLive();

        if (isLive()) {
            updateViewerPoints();

            if (minutesPassed % channelFile.getInt("checkPalsTime") == 0) {
                checkPals();
            }

            if (minutesPassed % channelFile.getInt("followsTime") == 0) {
                if (followsLoaded) {
                    checkNewFollows();
                } else {
                    if (!isLoading) {
                        loadFollows();
                        isLoading = true;
                    }
                }
            }

            minutesPassed += 1;
        }
    }

    private void configDefaults() {
        // Command Prefix
        channelFile.addDefault("prefix", "!");

        channelFile.addDefault("shouldWelcome", true);

        // Auto Clear settings
        channelFile.addDefault("autoClear", false);
        channelFile.addDefault("autoClearTime", 10);

        channelFile.addDefault("checkPalsTime", 10);
        channelFile.addDefault("followsTime", 1);

        // Points
        channelFile.addDefault("pointsPerMinute", 1);
    }

    private void checkIfLive() {
        JsonArray array = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + getChannelName()).getAsJsonObject().get("data").getAsJsonArray();

        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            if (obj.get("type") != null) {
                if (!isLive())
                    setLive(true);
            } else setLive(false);
        }
    }

    private void checkPals() {
        JsonArray array = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + getChannelName()).getAsJsonObject().get("data").getAsJsonArray();

        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();

            String id = obj.get("game_id").getAsString();

            StringBuilder str = new StringBuilder();
            int online = 0;
            for (String s : getChannelFile().getPals()) {
                JsonObject temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + s).getAsJsonObject();
                JsonArray tempArray = temp.get("data").getAsJsonArray();

                if (tempArray.size() > 0 && tempArray.get(0).getAsJsonObject().get("game_id").getAsString().equalsIgnoreCase(id)) {
                    str.append("/").append(s.replace("@", " ").trim());
                    online++;
                }
            }

            if (online > 0)
                messageChannel(String.format("Check out everyone's perspective at: https://multistre.am/%s%s/layout4/", getChannelName(), str.toString().trim()));
        }
    }

    private void loadFollows() {
        new Thread(() -> {
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
                timesRan++;
                if (timesRan >= 25) {
                    try {
                        Thread.sleep(1000 * 60);
                        timesRan = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            getChannelFile().set("follows", followers);
            followsLoaded = true;
            Main.getInstance().getConsole().println("[Bot]: finished loading followers...");
        }).start();
    }

    private JsonElement getViewerList(Channel channel) {
        return GetJsonData.getInstance().getJson(String.format("https://tmi.twitch.tv/group/user/%s/chatters", channel.getChannelName()));
    }

    private void checkNewFollows() {
        JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + getChannelID() + "&first=1");

        JsonElement follows = temp.getAsJsonObject().get("data");

        for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
            String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString().replace("\"", " ").trim();
            String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString().replace("\"", " ").trim();
            addFollower(user, followDate.substring(0, 10));
        }

        HashMap<String, String> followFile = getChannelFile().getHashMap("follows");

        ArrayList<String> newFollows = new ArrayList<>();

        for (String s : followers.keySet()) {
            if (!followFile.containsKey(s.toLowerCase())) {
                newFollows.add(s.toLowerCase());
            }
        }

        if (newFollows.size() > 0) {
            SortArrayList list = new SortArrayList(newFollows);
            list.sortAscending();

            StringBuilder str = new StringBuilder();
            for (String s : list.getArrayList()) {
                str.append(", ").append(s);
            }

            messageChannel(String.format("Thank you %s for the follow.", str.toString()));
            newFollows.clear();
            getChannelFile().set("follows", followers);
        }
    }

    private void updateViewerPoints() {
        JsonObject chatters = getViewerList(this).getAsJsonObject().get("chatters").getAsJsonObject();

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
        setViewers(viewers);

        if (channelFile.contains("points")) points = channelFile.getPointsMap();
        else points = new HashMap<>();

        for (String s : getViewers()) {
            s = s.toLowerCase();
            if (isFollowing(s)) {
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
