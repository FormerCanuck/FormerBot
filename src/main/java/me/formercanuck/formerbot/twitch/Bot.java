package me.formercanuck.formerbot.twitch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.fc.console.Console;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.connection.TwitchConnection;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot {

    private TwitchConnection twitchConnection;

    private ReadTwitchIRC readTwitchIRC;

    private CommandManager commandManager;

    private ArrayList<String> mods = new ArrayList<>();
    private ArrayList<String> whitelisted = new ArrayList<>();

    private ArrayList<String> remember = new ArrayList<>();

    private HashMap<String, String> followers = new HashMap<>();

    private String channel;
    private String channelID;

    private ConfigFile botFile;

    private Console console;
    private ArrayList<String> watchlist;

    public Bot() {
        twitchConnection = new TwitchConnection();
        readTwitchIRC = new ReadTwitchIRC(twitchConnection);
        console = Main.getInstance().getConsole();
    }

    public void connect() {
        sendRawMessage("CAP REQ :twitch.tv/tags");
        sendRawMessage("CAP REQ :twitch.tv/commands");
        sendRawMessage("PASS oauth:3tphuo7t7zhoz0os9we3vpwcpfhxur");
        sendRawMessage("NICK FormerB0t");

        new Thread(readTwitchIRC).start();
    }

    public void joinChannel(String channel) {
        this.channel = channel;
        sendRawMessage("JOIN " + channel);

        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users?login=" + channel.substring(1));

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();

            System.out.println(jsonElement.getAsJsonObject().get("data"));

            channelID = obj.get("id").getAsString();
        }

        botFile = new ConfigFile(channel.substring(1));

        if (!botFile.contains("prefix")) botFile.set("prefix", "!");

        if (!botFile.contains("autoClear")) {
            botFile.set("autoClear", false);
            botFile.set("autoClearTime", 10);
        }
        commandManager = new CommandManager();

        loadFollows();
    }

    public ReadTwitchIRC getReadTwitchIRC() {
        return readTwitchIRC;
    }

    public ConfigFile getBotFile() {
        return botFile;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void messageChannel(String message) {
        sendRawMessage(String.format("PRIVMSG %s :%s", channel, message));
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

    private void loadFollows() {
        JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + getChannelID() + "&first=100");

        console.println("[Bot]: loading followers...");

        while (temp.getAsJsonObject().get("pagination").getAsJsonObject().has("cursor")) {
            JsonElement follows = temp.getAsJsonObject().get("data");

            for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
                String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString().replace("\"", " ").trim();
                String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString().replace("\"", " ").trim();
                addFollower(user, followDate.substring(0, 10));
            }

            temp =
                    GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + getChannelID() + "&first=100&after=" + temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").getAsString().replace("\"", " ").trim());
            console.error(followers.size() + " followers loaded");
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        console.println("[Bot]: finished loading followers...");
    }

    public String getChannel() {
        return channel;
    }

    public void addRemember(String str) {
        this.remember.add(str);
    }

    public boolean isRememberEmpty() {
        return remember.isEmpty();
    }

    public String getRemember() {
        String temp = remember.get(0);
        remember.remove(0);
        return temp;
    }

    public void sendRawMessage(String message) {
        try {
            console.println("> " + message);
            twitchConnection.getToTwitch().write(String.format("%s %s", message, "\r\n"));
            twitchConnection.getToTwitch().flush();
        } catch (Exception e) {
            console.error(e.toString());
        }
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

    public String getChannelID() {
        return channelID;
    }
}
