package me.formercanuck.formerbot.twitch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.fc.console.Console;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.timertasks.DuelTask;
import me.formercanuck.formerbot.timertasks.Followers;
import me.formercanuck.formerbot.timertasks.UpdateViewers;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Channel {

    private String channel;

    private String channelID;

    public HashMap<String, String> followers = new HashMap<>();

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

    private Console console;

    private List<DuelTask> duels = new ArrayList<>();

    public Channel(String channel) {
        this.viewers = new ArrayList<>();
        this.channel = String.format("#%s", channel);
        this.bot = Main.getInstance().getBot();

        channelFile = new ConfigFile(getChannelName().toLowerCase());

        if (!channelFile.contains("prefix")) channelFile.set("prefix", "!");

        if (!channelFile.contains("autoClear")) {
            channelFile.set("autoClear", false);
            channelFile.set("autoClearTime", 10);
        }

        this.commandManager = new CommandManager(this);

        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users?login=" + getChannelName());

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();
            channelID = obj.get("id").getAsString();
        }

        hasChatted = new ArrayList<>();

        this.console = new Console(channel);
        readTwitchIRC = new ReadTwitchIRC(bot.getTwitchConnection(), this);
        new Thread(readTwitchIRC).start();

        Followers fllwrs = new Followers(this);
        Timer followerTimer = new Timer();
        followerTimer.scheduleAtFixedRate(fllwrs, 0, (1000 * 60) * 10);

        Timer updatePointsAndViewers = new Timer();
        updatePointsAndViewers.scheduleAtFixedRate(new UpdateViewers(this), 0, (1000 * 60));
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

    public List<String> getViewers() {
        return viewers;
    }

    public void setViewers(List<String> viewers) {
        this.viewers = viewers;
    }

    public HashMap<String, Integer> getPoints() {
        this.points = (HashMap<String, Integer>) channelFile.get("points");
        return this.points;
    }

    private void savePoints() {
        channelFile.set("points", points);
    }

    private void setPoints(String user, int points) {
        getPoints().put(user, points);
        savePoints();
    }

    public void addPoints(String user, int points) {
        user = user.toLowerCase();
        setPoints(user, getPoints().get(user) + points);
    }

    public void removePoints(String user, int points) {
        user = user.toLowerCase();
        setPoints(user, getPoints().get(user) - points);
    }

    public Console getConsole() {
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

    public void setLive(boolean isLive) {
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
//        bot.sendRawMessage(String.format("PRIVMSG %s :/me %s", getChannel(), message));
        console.println(String.format("PRIVMSG %s :/me %s", getChannel(), message), Color.ORANGE);
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

    public void addFollower(String user, String followDate) {
        followers.put(user.toLowerCase(), followDate);
    }

    public String getFollowDate(String user) {
        user = user.toLowerCase();
        HashMap<String, String> follows = (HashMap<String, String>) channelFile.get("follows");
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
            follows = (HashMap<String, String>) channelFile.get("follows");
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
}
