package me.formercanuck.formerbot.utils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiscUtils {

    // Pattern for recognizing a URL, based off RFC 3986
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public static String strip(String string) {
        return string.replace("\"", " ").trim();
    }

    public static int containsLink(String text) {
        Matcher matcher = urlPattern.matcher(text);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            System.out.println(matchStart + " : " + matchEnd);
            return matchEnd;
        }
        return 0;
    }

    static boolean isDigit(char check) {
        return Character.isDigit(check);
    }

    public static int calC(String input) {

        int len = input.length();
        ArrayList list1 = new ArrayList();
        ArrayList list2 = new ArrayList();

        for (int i = 0; i < len; i++) {
            if ((i + 1 <= len - 1)) {
                if (isDigit(input.charAt(i)) && isDigit(input.charAt(i + 1))) {
                    String temp = input.charAt(i) + "" + input.charAt(i + 1);
                    int toInt = Integer.parseInt(temp);
                    list1.add(toInt);
                    i = i + 1;
                } else if (isDigit(input.charAt(i))) {
                    list1.add(input.charAt(i) - '0');
                } else {
                    list2.add(input.charAt(i));
                }

            }
        }

        int result = 0;
        result = result + (int) list1.get(0);
        for (int t = 0; t < list2.size(); t++) {
            char oper = (char) list2.get(t);
            switch (oper) {
                case '*':
                    return result * (int) list1.get(t + 1);
                case '/':
                    return result / (int) list1.get(t + 1);
                case '-':
                    return result - (int) list1.get(t + 1);
                case '+':
                    return result + (int) list1.get(t + 1);
            }
        }
        return 0;
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
