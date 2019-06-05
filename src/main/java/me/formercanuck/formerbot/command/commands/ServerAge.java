package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class ServerAge extends Command {

    @Override
    public String getName() {
        return "serverage";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        if (!bot.isMod(sender) || args.size() < 1) {
            if (bot.getBotFile().contains("serverage")) {
                bot.messageChannel(String.format("%s, the server has been live since: %s, which is %s days", sender, bot.getBotFile().getString("serverage"), numberOfDaysLive()));
                return;
            } else {
                bot.messageChannel(String.format("%s,there currently isn't a start date for the server.", sender));
                return;
            }
        }

        if (args.get(0).equalsIgnoreCase("set")) {
            bot.getBotFile().set("serverage", args.get(1));
            bot.messageChannel(String.format("%s successfully set the start date of the server to %s.", sender, args.get(1)));
            return;
        }

        if (args.get(0).equalsIgnoreCase("clear")) {
            bot.getBotFile().remove("serverage");
            bot.messageChannel(String.format("%s successfully cleared the start date of the server.", sender));
        }
    }

    private Long numberOfDaysLive() {
        Bot bot = Main.getInstance().getBot();
        if (!bot.getBotFile().contains("serverage")) return 0L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = sdf.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
            date2 = sdf.parse(bot.getBotFile().getString("serverage"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ChronoUnit.DAYS.between(date2.toInstant(), date1.toInstant());
    }
}
