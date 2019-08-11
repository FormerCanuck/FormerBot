package me.formercanuck.formerbot.timertasks;

import me.formercanuck.formerbot.twitch.Channel;

import java.util.TimerTask;

public class Update extends TimerTask {

    private Channel channel;

    public Update(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        channel.update();
    }
}
