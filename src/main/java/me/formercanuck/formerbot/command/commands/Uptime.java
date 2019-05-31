package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
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

    public void onCommand(String sender, String channel, String[] args) {
        channel = channel.substring(1);

        JsonElement temp = jsonData.getJson("https://api.twitch.tv/kraken/streams/" + channel);

        if (temp.isJsonObject()) {
            JsonObject obj = temp.getAsJsonObject();

            JsonElement element = null;

            try {
                element = obj.get("stream").getAsJsonObject();
            } catch (Exception e) {
                temp = jsonData.getJson("https://api.twitch.tv/kraken/channels/formercanuck");
//                System.out.println(temp.toString());
                Main.getInstance().getBot().messageChannel(String.format("%s, %s is not currently live!", sender, temp.getAsJsonObject().get("display_name").toString().replace("\"", "")));
                return;
            }

            String uptime = element.getAsJsonObject().get("created_at").getAsString().replace("\"", "");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date parse = null;
            try {
                parse = format.parse(uptime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = System.currentTimeMillis() - parse.getTime();

//            int hours = (int) (diff / 3600000);
//            int remainder = (int) (diff - hours * 3600000);
//            int mins = (remainder / 60000);
//            remainder = (remainder - mins * 60000);
//            int secs = remainder / 1000;

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;

            if (days > 0) {
                Main.getInstance().getBot().messageChannel(String.format("%s, %s has been live for: %s day(s) %s hour(s) %s minute(s) and %s seconds.", sender, channel, days, hours % 24, minutes % 60, seconds % 60));
            } else if (hours > 0) {
                Main.getInstance().getBot().messageChannel(String.format("%s, %s has been live for: %s hour(s) %s minute(s) and %s seconds.", sender, channel, hours % 24, minutes % 60, seconds % 60));
            } else if (minutes > 0) {
                Main.getInstance().getBot().messageChannel(String.format("%s, %s has been live for: %s minute(s) and %s seconds.", sender, channel, minutes % 60, seconds % 60));
            } else if (seconds > 0) {
                Main.getInstance().getBot().messageChannel(String.format("%s, %s has been live for: %s seconds.", sender, channel, seconds % 60));
            }
        }
    }
}
