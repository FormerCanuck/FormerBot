package me.formercanuck.formerbot;

import me.fc.console.Console;
import me.formercanuck.formerbot.twitch.Bot;

public class Main {


    private Bot bot;

    private static Main instance;

    private Main() {
        instance = this;

        bot = new Bot();
        bot.connect();
//
//        bot.joinChannel(new Scanner(System.in).next());
    }

    public Console getConsole() {
        return bot.getConsole();
    }

    public Bot getBot() {
        return bot;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new Main();
    }
}
