package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Followage extends Command {
    @Override
    public String getName() {
        return "followage";
    }

    private Bot bot;

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        bot = Main.getInstance().getBot();
        if (args.size() == 0) {
            getFollowage(sender, channel);
        } else if (args.size() == 1) {
            if (!args.get(0).equalsIgnoreCase("top") && bot.isMod(sender)) {
                getFollowage(args.get(0), channel);
                return;
            }

            JsonObject chatters = getViewerList().getAsJsonObject().get("chatters").getAsJsonObject();

            List<String> viewers = new ArrayList<>();
            Map<String, Long> followers = new HashMap<String, Long>();

            JsonArray vip = chatters.get("vips").getAsJsonArray();
            JsonArray moderators = chatters.get("moderators").getAsJsonArray();
            JsonArray staff = chatters.get("staff").getAsJsonArray();
            JsonArray admins = chatters.get("admins").getAsJsonArray();
            JsonArray global_mods = chatters.get("global_mods").getAsJsonArray();
            JsonArray views = chatters.get("viewers").getAsJsonArray();

            for (JsonElement element : vip) {
                viewers.add(strip(element.toString()));
            }

            for (JsonElement element : moderators) {
                viewers.add(strip(element.toString()));
            }

            for (JsonElement element : staff) {
                viewers.add(strip(element.toString()));
            }

            for (JsonElement element : admins) {
                viewers.add(strip(element.toString()));
            }

            for (JsonElement element : global_mods) {
                viewers.add(strip(element.toString()));
            }

            for (JsonElement element : views) {
                viewers.add(strip(element.toString()));
            }

            for (String viewer : viewers) {
                if (bot.isFollowing(viewer)) {
                    followers.put(viewer, numberOfDaysFollowed(viewer));
                }
            }

            HashMap<String, Long> sortedFollowers = new HashMap<>();
            sortedFollowers = followers
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

            StringBuilder builder = new StringBuilder();

            builder.append("Here is a list of the top 5 followers: ");

            int i = 1;

            for (String viewer : putFirstEntries(5, sortedFollowers).keySet()) {
                builder.append(String.format("%s: %s (%s) ", i++, viewer, sortedFollowers.get(viewer)));
            }
            bot.messageChannel(builder.toString());
        }
    }

    private HashMap<String, Long> putFirstEntries(int max, HashMap<String, Long> source) {
        int count = 0;
        HashMap<String, Long> target = new HashMap<String, Long>();
        for (Map.Entry<String, Long> entry : source.entrySet()) {
            if (count >= max) break;

            target.put(entry.getKey(), entry.getValue());
            count++;
        }
        return target;
    }

    private String strip(String string) {
        return string.replace("\"", " ").trim();
    }

    public JsonElement getViewerList() {
        return GetJsonData.getInstance().getJson(String.format("https://tmi.twitch.tv/group/user/%s/chatters", bot.getChannel().substring(1)));
    }

    public void getFollowage(String user, String channel) {
        if (bot.isFollowing(user.toLowerCase())) {
            bot.messageChannel(String.format("%s has been following @%s since %s, which is %s days", user, channel.substring(1), bot.getFollowDate(user), numberOfDaysFollowed(user)));
        } else {
            bot.messageChannel(String.format("%s, you are not following.", user));
        }
    }

    private Long numberOfDaysFollowed(String viewer) {
        if (!bot.isFollowing(viewer)) return 0L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = sdf.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
            date2 = sdf.parse(bot.getFollowDate(viewer));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ChronoUnit.DAYS.between(date2.toInstant(), date1.toInstant());
    }
}
