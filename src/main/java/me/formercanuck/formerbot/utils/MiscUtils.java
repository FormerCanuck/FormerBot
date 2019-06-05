package me.formercanuck.formerbot.utils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class MiscUtils {

    public static String strip(String string) {
        return string.replace("\"", " ").trim();
    }

    @NotNull
    public static Long numberOfDaysBetweenDateAndNow(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = sdf.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
            date2 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert date2 != null;
        return ChronoUnit.DAYS.between(date2.toInstant(), date1.toInstant());
    }
}
