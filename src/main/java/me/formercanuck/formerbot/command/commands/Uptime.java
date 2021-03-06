package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Uptime extends Command {

    private GetJsonData jsonData = GetJsonData.getInstance();

    public String getName() {
        return "uptime";
    }

    public void onCommand(String sender, Channel channel, String[] args) {
        JsonElement temp = jsonData.getJson("https://api.twitch.tv/helix/streams?user_login=" + channel.getChannelName());

        if (temp.isJsonObject()) {
            JsonObject obj = temp.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();

            String uptime = obj.get("started_at").getAsString().replace("\"", "");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date parse = null;
            try {
                parse = format.parse(uptime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert parse != null;
            long diff = System.currentTimeMillis() - parse.getTime();

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                channel.messageChannel(String.format("%s, %s has been live for: %s day(s) %s hour(s) %s minute(s) and %s seconds.", sender, channel.getChannelName(), days, hours % 24, minutes % 60, seconds % 60));
            } else if (hours > 0) {
                channel.messageChannel(String.format("%s, %s has been live for: %s hour(s) %s minute(s) and %s seconds.", sender, channel.getChannelName(), hours % 24, minutes % 60, seconds % 60));
            } else if (minutes > 0) {
                channel.messageChannel(String.format("%s, %s has been live for: %s minute(s) and %s seconds.", sender, channel.getChannelName(), minutes % 60, seconds % 60));
            } else if (seconds > 0) {
                channel.messageChannel(String.format("%s, %s has been live for: %s seconds.", sender, channel.getChannelName(), seconds % 60));
            }
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: !uptime, displays how long the stream has been live.";
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
