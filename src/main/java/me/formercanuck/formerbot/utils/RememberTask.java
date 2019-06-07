package me.formercanuck.formerbot.utils;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;

import java.util.TimerTask;

public class RememberTask extends TimerTask {

    private Bot bot = Main.getInstance().getBot();

    @Override
    public void run() {
        if (!bot.isRememberEmpty())
            bot.messageChannel("Don't forget, " + Main.getInstance().getBot().getRemember());
    }
}
