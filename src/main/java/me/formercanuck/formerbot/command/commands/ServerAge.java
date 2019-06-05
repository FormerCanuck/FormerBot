package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonElement;
import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.utils.GetJsonData;
import me.formercanuck.formerbot.utils.MiscUtils;

import java.util.ArrayList;

public class ServerAge extends Command {

    @Override
    public String getName() {
        return "serverage";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        ConfigFile botFile = bot.getBotFile();

        JsonElement streams = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + channel.substring(1));
        String game_id = MiscUtils.strip(streams.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("game_id").toString());
        String game = "serverage." + MiscUtils.strip(GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/games?id=" + game_id).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("name").toString());

        if (!bot.isMod(sender) || args.size() < 1) {
            if (botFile.contains(game)) {
                bot.messageChannel(String.format("%s, the server has been live since: %s, which is %s days", sender, botFile.getString(game), MiscUtils.numberOfDaysBetweenDateAndNow(botFile.getString(game))));
                return;
            } else {
                bot.messageChannel(String.format("%s,there currently isn't a start date for the server.", sender));
                return;
            }
        }

        if (args.get(0).equalsIgnoreCase("set")) {
            botFile.set(game, args.get(1));
            bot.messageChannel(String.format("%s successfully set the start date of the server to %s.", sender, args.get(1)));
            return;
        }

        if (args.get(0).equalsIgnoreCase("clear")) {
            botFile.remove(game);
            bot.messageChannel(String.format("%s successfully cleared the start date of the server.", sender));
        }
    }
}
