package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonElement;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.GetJsonData;
import me.formercanuck.formerbot.utils.MiscUtils;

public class ServerAge extends Command {

    @Override
    public String getName() {
        return "serverage";
    }

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        Bot bot = Main.getInstance().getBot();
        ConfigFile channelFile = channel.getChannelFile();

        JsonElement streams = GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/streams?user_login=" + channel.getChannelName());
        String game_id = MiscUtils.strip(streams.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("game_id").toString());
        String game = "serverage." + MiscUtils.strip(GetJsonData.getInstance().getJson("https://api.twitch.tv/helix/games?id=" + game_id).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("name").toString());

        if (!channel.isMod(sender) || !channel.getWhitelisted().contains(sender.toLowerCase()) && args.length < 1) {
            if (channelFile.contains(game)) {
                channel.messageChannel(String.format("%s, the server has been live since: %s, which is %s days", sender, channelFile.getString(game), MiscUtils.numberOfDaysBetweenDateAndNow(channelFile.getString(game))));
                return;
            } else {
                channel.messageChannel("Usage: !serverage <set | clear> <start date ex: 2019-06-07>");
                return;
            }
        }

        if (args[0].equalsIgnoreCase("set")) {
            channelFile.set(game, args[1]);
            channel.messageChannel(String.format("%s successfully set the start date of the server to %s.", sender, args[1]));
            return;
        }

        if (args[0].equalsIgnoreCase("clear")) {
            channelFile.remove(game);
            channel.messageChannel(String.format("%s successfully cleared the start date of the server.", sender));
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: Mod > !serverage <server start date> to set start date of current server, Everyone > !server will display when the server started.";
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
