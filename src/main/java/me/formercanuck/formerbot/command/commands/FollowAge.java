package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.utils.GetJsonData;
import me.formercanuck.formerbot.utils.MiscUtils;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class FollowAge extends Command {
    @Override
    public String getName() {
        return "followage";
    }

    private Bot bot;

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        bot = Main.getInstance().getBot();
        cooldown();
        if (args.length == 0) {
            getFollowage(sender, channel);
        } else if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("top") && bot.getChannel().isMod(sender) || Main.getInstance().getBot().getChannel().isWhiteListed(sender)) {
                getFollowage(args[0], channel);
                return;
            }

            JsonObject chatters = getViewerList().getAsJsonObject().get("chatters").getAsJsonObject();

            List<String> viewers = new ArrayList<>();
            HashMap<String, Long> followers = new HashMap<>();

            JsonArray vip = chatters.get("vips").getAsJsonArray();
            JsonArray moderators = chatters.get("moderators").getAsJsonArray();
            JsonArray staff = chatters.get("staff").getAsJsonArray();
            JsonArray admins = chatters.get("admins").getAsJsonArray();
            JsonArray global_mods = chatters.get("global_mods").getAsJsonArray();
            JsonArray views = chatters.get("viewers").getAsJsonArray();

            for (JsonElement element : vip) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : moderators) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : staff) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : admins) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : global_mods) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (JsonElement element : views) {
                viewers.add(MiscUtils.strip(element.toString()));
            }

            for (String viewer : viewers) {
                if (bot.getChannel().isFollowing(viewer)) {
                    followers.put(viewer, MiscUtils.numberOfDaysBetweenDateAndNow(bot.getChannel().getFollowDate(viewer)));
                }
            }


            StringBuilder builder = new StringBuilder();

            builder.append("Here is a list of the top 5 followers: ");

            int i = 1;

            for (String viewer : putFirstEntries(followers).keySet()) {
                builder.append(String.format("%s: %s (%s) ", i++, viewer, followers.get(viewer)));
            }
            bot.getChannel().messageChannel(builder.toString());
        }
    }

    @Override
    public int getCooldown() {
        return 1;
    }

    private HashMap<String, Long> putFirstEntries(HashMap<String, Long> source) {
        int count = 0;
        HashMap<String, Long> target = new HashMap<>();
        for (Map.Entry<String, Long> entry : source.entrySet()) {
            if (count >= 5) break;

            target.put(entry.getKey(), entry.getValue());
            count++;
        }

        return target
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    private JsonElement getViewerList() {
        return GetJsonData.getInstance().getJson(String.format("https://tmi.twitch.tv/group/user/%s/chatters", bot.getChannel().getChannelName()));
    }

    private void getFollowage(String user, String channel) {
        if (bot.getChannel().isFollowing(user.toLowerCase())) {
            bot.getChannel().messageChannel(String.format("%s has been following @%s since %s, which is %s days", user, channel.substring(1), bot.getChannel().getFollowDate(user), MiscUtils.numberOfDaysBetweenDateAndNow(bot.getChannel().getFollowDate(user))));
        } else {
            bot.getChannel().messageChannel(String.format("%s, you are not following.", user));
        }
    }
}
