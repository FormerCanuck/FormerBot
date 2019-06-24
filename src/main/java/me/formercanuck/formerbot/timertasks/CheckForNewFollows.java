package me.formercanuck.formerbot.timertasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.TimerTask;

public class CheckForNewFollows extends TimerTask {

    Bot bot = Main.getInstance().getBot();

    public static void main(String[] args) {
        System.out.println();
    }

    @Override
    public void run() {
        JsonElement jsonElement = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/users/follows?first=1&to_id=" + Main.getInstance().getBot().getChannel().getChannelID());

        String lastFollow = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("from_name").getAsString();
        if (bot.getChannel().getLastFollow().isEmpty()) bot.getChannel().setLastFollow(lastFollow);
        else {
            if (bot.getChannel().isHasCheckedFirstFollow())
                if (!lastFollow.equalsIgnoreCase(bot.getChannel().getLastFollow())) {
                    bot.getChannel().setLastFollow(lastFollow);
                    bot.getChannel().messageChannel(String.format("Thanks for the follow @%s!", lastFollow));
                } else System.out.println("No new follows");
            else bot.getChannel().setHasCheckedFirstFollow();
        }

        JsonObject obj = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + bot.getChannel().getChannelName()).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();

        if (obj.get("type") != null) {
            if (!bot.getChannel().isLive())
                bot.getChannel().setLive(true);
        } else bot.getChannel().setLive(false);

        String id = obj.get("game_id").getAsString();

        StringBuilder str = new StringBuilder();
        int online = 0;
        for (String s : bot.getBotFile().getPals()) {
            JsonObject temp = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + s).getAsJsonObject();
            JsonArray tempArray = temp.get("data").getAsJsonArray();

            if (tempArray.size() > 0 && tempArray.get(0).getAsJsonObject().get("game_id").getAsString().equalsIgnoreCase(id)) {
                str.append("/").append(s.replace("@", " ").trim());
                online++;
            }
        }
        if (online > 0)
            bot.getChannel().messageChannel(String.format("Check out everyone's perspective at: https://multistre.am/%s%s/layout4/", Main.getInstance().getBot().getChannel().getChannelName(), str.toString().trim()));
    }
}
