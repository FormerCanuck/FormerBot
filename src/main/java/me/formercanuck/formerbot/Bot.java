package me.formercanuck.formerbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.command.CommandManager;
import me.formercanuck.formerbot.connection.ReadTwitchIRC;
import me.formercanuck.formerbot.connection.TwitchConnection;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    public void messageChannel(String message) {
        sendRawMessage(String.format("PRIVMSG %s :%s", channel, message));
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
        Iterator<Map.Entry<String, String>>
                iterator = followers.entrySet().iterator();

        // flag to store result
        boolean isKeyPresent = false;

        // Iterate over the HashMap
        while (iterator.hasNext()) {

            // Get the entry at this iteration
            Map.Entry<String, String> entry = iterator.next();

            // Check if this key is the required key
            if (user == entry.getKey()) {
                isKeyPresent = true;
            }
        }
        return isKeyPresent;
    }

    private void loadFollows() {
//        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/kraken/channels/" + channel.substring(1));
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/kraken/channels/recanem");

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject();

            String id = obj.get("_id").getAsString();

            JsonElement temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + id + "&first=100");

            while (temp.getAsJsonObject().get("pagination").getAsJsonObject().has("cursor")) {
                JsonElement follows = temp.getAsJsonObject().get("data");

                for (int i = 0; i < follows.getAsJsonArray().size(); i++) {
                    String user = follows.getAsJsonArray().get(i).getAsJsonObject().get("from_name").toString();
                    String followDate = follows.getAsJsonArray().get(i).getAsJsonObject().get("followed_at").toString();
                    System.out.println(String.format("%s has been following since %s", user, followDate));
                    addFollower(user, followDate);
                }

                temp =
                        GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?to_id=" + id + "&first=100&after=" + temp.getAsJsonObject().get("pagination").getAsJsonObject().get("cursor").getAsString().replace("\"", " ").trim());
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(followers.size());
            }
            System.out.println("Loaded all followers!");
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
