package me.formercanuck.formerbot.timertasks;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.twitch.Bot;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.TimerTask;

public class RememberTask extends TimerTask {

    private Bot bot = Main.getInstance().getBot();

    private Channel channel;

    public RememberTask(Channel channel) {
        this.channel = channel;
    }


    @Override
    public void run() {
        if (!bot.isRememberEmpty())
            channel.messageChannel("Don't forget, " + Main.getInstance().getBot().getRemember());
    }
}
