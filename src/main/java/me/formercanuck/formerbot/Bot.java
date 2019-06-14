package me.formercanuck.formerbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.fc.console.Console;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.connection.TwitchConnection;
import me.formercanuck.formerbot.files.CommandFile;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.ArrayList;
import java.util.HashMap;

public class Bot {

    private TwitchConnection twitchConnection;

    private ReadTwitchIRC readTwitchIRC;

    private CommandManager commandManager;

    private ArrayList<String> mods = new ArrayList<>();
    private ArrayList<String> whitelisted = new ArrayList<>();

    private ArrayList<String> remember = new ArrayList<>();

    private HashMap<String, String> followers = new HashMap<>();

    private String channel;

    private ConfigFile botFile;
    private CommandFile commandFile;

    private Console console;

    Bot() {
        twitchConnection = new TwitchConnection();
        readTwitchIRC = new ReadTwitchIRC(twitchConnection);
        commandManager = new CommandManager();
        console = Main.getInstance().getConsole();
    }

    void connect() {
        sendRawMessage("CAP REQ :twitch.tv/tags");
        sendRawMessage("CAP REQ :twitch.tv/commands");
        sendRawMessage("PASS oauth:3tphuo7t7zhoz0os9we3vpwcpfhxur");
        sendRawMessage("NICK FormerB0t");

        new Thread(readTwitchIRC).start();
    }

    void joinChannel(String channel) {
        this.channel = channel;
        sendRawMessage("JOIN " + channel);

        botFile = new ConfigFile(channel.substring(1));
        commandFile = new CommandFile(channel.substring(1));

        if (!botFile.contains("autoClear")) {
            botFile.set("autoClear", false);
            botFile.set("autoClearTime", 10);
        }

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

    public void setWhitelisted(ArrayList<String> whitelisted) {
        this.whitelisted = whitelisted;
    }

    private void loadFollows() {
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users?login=" + channel.substring(1));

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();

            String id = obj.get("id").getAsString();

            JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + id + "&first=100");

            console.println("[Bot]: loading followers...");

            while (temp.getAsJsonObject().get("pagination").getAsJsonObject().has("cursor")) {
                JsonElement follows = temp.getAsJsonObject().get("data");

                for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
                    String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString().replace("\"", " ").trim();
                    String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString().replace("\"", " ").trim();
                    addFollower(user, followDate.substring(0, 10));
                }

                temp =
                        GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + id + "&first=100&after=" + temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").getAsString().replace("\"", " ").trim());
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            console.println("[Bot]: finished loading followers...");
        }
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

    public CommandFile getCommandFile() {
        return commandFile;
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
}
