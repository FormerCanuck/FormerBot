package me.formercanuck.formerbot;

import me.fc.console.Console;

public class Main {

    private Console console;

    private Bot bot;

    private static Main instance;

    private Main() {
        instance = this;
        console = new Console("FormerB0t");

        bot = new Bot();
        bot.connect();

        bot.sendRawMessage("JOIN #ulincsys");
    }

    public Console getConsole() {
        return console;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new Main();
    }
}
