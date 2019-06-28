package me.formercanuck.formerbot.utils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class MiscUtils {

    public static String strip(String string) {
        return string.replace("\"", " ").trim();
    }

    private static boolean isDigit(char check) {
        return Character.isDigit(check);
    }

    public static HashMap<String, Long> putFirstEntries(HashMap<String, Long> source) {
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

    public static String getDateString(int days) {
        int year = (days / 365);
        days = days % 365;
        int week = days / 7;
        days = days % 7;

        StringBuilder str = new StringBuilder();

        if (year > 0) str.append(year).append(" years, ");

        if (week > 0) str.append(week).append(" weeks, and ");

        if (days > 0) str.append(days).append(" days");

        return str.toString().trim();
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
