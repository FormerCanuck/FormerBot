package me.formercanuck.formerbot.timertasks;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Bot;

import java.util.TimerTask;

public class RememberTask extends TimerTask {

    private Bot bot = Main.getInstance().getBot();

    @Override
    public void run() {
        if (!bot.isRememberEmpty())
            bot.messageChannel("Don't forget, " + Main.getInstance().getBot().getRemember());
    }
}
