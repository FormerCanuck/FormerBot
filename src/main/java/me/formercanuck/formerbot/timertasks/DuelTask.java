package me.formercanuck.formerbot.timertasks;

import me.formercanuck.formerbot.twitch.Channel;

import java.util.TimerTask;

public class DuelTask extends TimerTask {

    private Channel channel;

    private String user1, user2;

    private int amt;

    private String id;


    public DuelTask(Channel channel, String user1, String user2, int amt) {
        this.channel = channel;
        this.user1 = user1;
        this.user2 = user2;
        this.amt = amt;

        this.id = user2;
    }

    public String getChallenger() {
        return user1;
    }

    public int getAmt() {
        return amt;
    }

    @Override
    public void run() {
        channel.getDuels().remove(this);
        channel.messageChannel(String.format("%s has hid in fear. Duel has been canceled.", user2));
    }

    public String getID() {
        return id;
    }
}
