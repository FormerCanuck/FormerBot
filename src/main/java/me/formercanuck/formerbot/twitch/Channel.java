package me.formercanuck.formerbot.twitch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.fc.console.Console;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.timertasks.CheckForNewFollows;
import me.formercanuck.formerbot.utils.Followers;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class Channel {

    private String channel;

    private String channelID;

    public HashMap<String, String> followers = new HashMap<>();

    private Bot bot;
    private CommandManager commandManager;
    private ReadTwitchIRC readTwitchIRC;

    private boolean shouldListen = true;
    private boolean isLive;
    private boolean hasCheckedFirstFollow = false;

    private ArrayList<String> mods = new ArrayList<>();
    private ArrayList<String> whitelisted = new ArrayList<>();
    private ArrayList<String> watchlist;
    private ConfigFile channelFile;
    private HashMap<String, ListenBot> listenBotHashMap = new HashMap<>();

    private List<String> hasChatted;

    private String lastFollow = "";
    private Console console;

    public Channel(String channel) {
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
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new CheckForNewFollows(this), 0, (1000 * 60) * 30);

        hasChatted = new ArrayList<>();

        this.console = new Console(channel);
        readTwitchIRC = new ReadTwitchIRC(bot.getTwitchConnection(), this);
        new Thread(readTwitchIRC).start();

        Followers fllwrs = new Followers(this);
        Timer followerTimer = new Timer();
        followerTimer.scheduleAtFixedRate(fllwrs, 0, (1000 * 60) * 10);
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

    public void toggleListen(String channel) {
        if (!listenBotHashMap.containsKey(channel)) {
            listenBotHashMap.put(channel, new ListenBot(channel));
        } else if (listenBotHashMap.containsKey(channel)) {
            listenBotHashMap.get(channel).stop();
            listenBotHashMap.remove(channel);
        }

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
        if (isLive) goLive();
        this.isLive = isLive;
    }

    public void setHasCheckedFirstFollow() {
        hasCheckedFirstFollow = true;
    }

    public boolean isHasCheckedFirstFollow() {
        return hasCheckedFirstFollow;
    }

    public String getLastFollow() {
        return lastFollow;
    }

    public void setLastFollow(String lastFollow) {
        this.lastFollow = lastFollow;
    }

    void join() {
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

    public void addFollower(String user, String followDate) {
        followers.put(user.toLowerCase(), followDate);
    }

    public String getFollowDate(String user) {
        return followers.get(user.toLowerCase());
    }

    public boolean isFollowing(String user) {
        HashMap<String, String> follows;
        follows = (HashMap<String, String>) channelFile.get("follows");

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
