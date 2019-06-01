package me.formercanuck.formerbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.connection.TwitchConnection;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.ArrayList;
import java.util.HashMap;

public class Bot {

    private TwitchConnection twitchConnection;

    private ReadTwitchIRC readTwitchIRC;

    private CommandManager commandManager;

    private ArrayList<String> mods = new ArrayList<>();

    private HashMap<String, String> followers = new HashMap<>();

    private String channel;

    private final String CLIENT_ID = "pqi99elyam4p8ewyab8eyrxnb8urvw";

    public Bot() {
        twitchConnection = new TwitchConnection();
        readTwitchIRC = new ReadTwitchIRC(twitchConnection);
        commandManager = new CommandManager();
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
        loadFollows();
    }

    public TwitchConnection getTwitchConnection() {
        return twitchConnection;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public boolean messageChannel(String message) {
        return sendRawMessage("PRIVMSG " + channel + " :" + message);
    }

    public void addMod(String user) {
        mods.add(user);
    }

    public ArrayList<String> getMods() {
        return mods;
    }

    public boolean isMod(String user) {
        return mods.contains(user.toLowerCase());
    }

    public void addFollower(String user, String followDate) {
        followers.put(user.toLowerCase(), followDate);
    }

    public String getFollowDate(String user) {
        return followers.get(user);
    }

    public HashMap<String, String> getFollowers() {
        return followers;
    }

    public boolean isFollowing(String user) {
        return followers.containsKey(user);
    }

    private void loadFollows() {
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/kraken/channels/" + channel.substring(1));

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject();

            String id = obj.get("_id").getAsString();

            JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + id);

            JsonElement follows = temp.getAsJsonObject().get("data");

            for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
                String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString();
                String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString();
                System.out.println(user + " " + followDate);
                addFollower(user, followDate);
            }

            if (temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").toString().isEmpty()) {
                System.out.println("EMPTY");
            } else
                loadMoreFollows(temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").toString());
        }
    }

    private void loadMoreFollows(String cursor) {
        cursor = cursor.substring(1, cursor.length() - 1);
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/kraken/channels/" + channel.substring(1));

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject();

            String id = obj.get("_id").getAsString();

            JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + id + "&after=" + cursor);

            if (!(temp instanceof JsonObject)) return;

            System.out.println(temp.getAsJsonObject().toString());

            JsonElement follows = temp.getAsJsonObject().get("data");

            for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
                String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString();
                String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString();
                System.out.println(user + " " + followDate);
                addFollower(user, followDate);
            }

            if (temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").toString().isEmpty()) {
                System.out.println("EMPTY");
            } else
                loadMoreFollows(temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").toString());
            System.out.println(follows.toString());
        }
    }

    public String getChannel() {
        return channel;
    }

    public boolean sendRawMessage(String message) {
        try {
            Main.getInstance().getConsole().println("> " + message);
            twitchConnection.getToTwitch().write(String.format("%s %s", message, "\r\n"));
            twitchConnection.getToTwitch().flush();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
