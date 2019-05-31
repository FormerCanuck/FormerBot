package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.utils.GetJsonData;

public class TopClips extends Command {
    @Override
    public String getName() {
        return "topclips";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        channel = channel.substring(1);
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/kraken/channels/" + channel);

        if (jsonElement.isJsonObject()) {
            JsonObject obj = jsonElement.getAsJsonObject();

            String id = obj.get("_id").getAsString();

            JsonElement temp = GetJsonData.getInstance().getJson(String.format("https://api.twitch.tv/helix/clips?broadcaster_id=%s&first=5", id));

            JsonArray jsonArray = temp.getAsJsonObject().get("data").getAsJsonArray();

            StringBuilder msg = new StringBuilder();

            msg.append("Check out the top 5 clips: ");

            for (int i = 0; i < 4; i++) {
                msg.append(String.format("Clip name: %s and the link: %s", jsonArray.get(i).getAsJsonObject().get("title").getAsString(), jsonArray.get(i).getAsJsonObject().get("url").getAsString()));
            }
            Main.getInstance().getBot().messageChannel(msg.toString());
        }
    }
}
