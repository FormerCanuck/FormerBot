package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;
import me.formercanuck.formerbot.utils.GetJsonData;
import me.formercanuck.formerbot.utils.MiscUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FollowAge extends Command {
    @Override
    public String getName() {
        return "followage";
    }

    private Bot bot;

    @Override
    public void onCommand(String sender, Channel channel, String[] args) {
        bot = Main.getInstance().getBot();
        cooldown(channel);
        if (args.length == 0) {
            getFollowage(sender, channel.getChannelName());
        } else if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("top") && channel.isMod(sender) || channel.isWhiteListed(sender)) {
                getFollowage(args[0], channel.getChannelName());
                return;
            }

            JsonObject chatters = getViewerList(channel).getAsJsonObject().get("chatters").getAsJsonObject();

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
                if (channel.isFollowing(viewer)) {
                    followers.put(viewer, MiscUtils.numberOfDaysBetweenDateAndNow(channel.getFollowDate(viewer)));
                }
            }


            StringBuilder builder = new StringBuilder();

            builder.append("Here is a list of the top 5 followers: ");

            int i = 1;

            for (String viewer : MiscUtils.putFirstEntries(followers).keySet()) {
                builder.append(String.format("%s: %s (%s) ", i++, viewer, followers.get(viewer)));
            }
            channel.messageChannel(builder.toString());
        }
    }

    @Override
    public String getUsage(Channel channel) {
        return "Usage: Non-Mod > !followage, (optional) Mod > !followage <user>";
    }

    @Override
    public int getCooldown() {
        return 1;
    }

    private JsonElement getViewerList(Channel channel) {
        return GetJsonData.getInstance().getJson(String.format("https://tmi.twitch.tv/group/user/%s/chatters", channel.getChannelName()));
    }

    private void getFollowage(String user, String channel) {
        if (bot.getChannel(channel).isFollowing(user.toLowerCase())) {
            bot.getChannel(channel).messageChannel(String.format("%s has been following @%s since %s, which is %s days", user, channel, bot.getChannel(channel).getFollowDate(user), MiscUtils.numberOfDaysBetweenDateAndNow(bot.getChannel(channel).getFollowDate(user))));
        } else {
            bot.getChannel(channel).messageChannel(String.format("%s, you are not following.", user));
        }
    }
}
