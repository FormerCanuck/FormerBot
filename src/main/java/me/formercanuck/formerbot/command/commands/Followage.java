package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class Followage extends Command {
    @Override
    public String getName() {
        return "followage";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        if (args.size() == 0) {
            getFollowage(sender, channel);
        } else if (args.size() == 1 && Main.getInstance().getBot().isMod(sender)) {
            getFollowage(args.get(0), channel);
        }
    }

    public void getFollowage(String user, String channel) {
        if (Main.getInstance().getBot().isFollowing(user.toLowerCase())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = null;
            Date date2 = null;

            try {
                date1 = sdf.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
                date2 = sdf.parse(Main.getInstance().getBot().getFollowDate(user));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Main.getInstance().getBot().messageChannel(String.format("%s has been following @%s since %s, which is %s days", user, channel.substring(1), Main.getInstance().getBot().getFollowDate(user), ChronoUnit.DAYS.between(date2.toInstant(), date1.toInstant())));
        } else {
            Main.getInstance().getBot().messageChannel(String.format("%s, you are not following.", user));
        }
    }
}
