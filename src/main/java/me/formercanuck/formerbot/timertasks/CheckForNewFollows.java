package me.formercanuck.formerbot.timertasks;

import com.google.gson.JsonElement;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.TimerTask;

public class CheckForNewFollows extends TimerTask {

    Bot bot = Main.getInstance().getBot();

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
    }
}
