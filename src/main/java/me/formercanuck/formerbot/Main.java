package me.formercanuck.formerbot;

import me.fc.console.Console;
import me.formercanuck.formerbot.twitch.Bot;

import java.util.Scanner;

public class Main {

    private Console console;

    private Bot bot;

    private static Main instance;

    private Main() {
        instance = this;
        console = new Console("FormerB0t");

        bot = new Bot();
        bot.connect();

        bot.joinChannel(new Scanner(System.in).next());
    }

    public Console getConsole() {
        return console;
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
