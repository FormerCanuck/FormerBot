package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.timertasks.DuelTask;
import me.formercanuck.formerbot.twitch.Channel;

import java.util.Random;

public class Accept extends Command {

    private Random random = new Random();

    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getUsage(Channel channel) {
        return "";
    }

    @Override
    public void onCommand(String sender, Channel channel, String... args) {
        if (channel.getDuel(sender) != null) {
            DuelTask duel = channel.getDuel(sender);
            if (random.nextBoolean()) {
                channel.addPoints(sender, duel.getAmt());
                channel.removePoints(duel.getChallenger(), duel.getAmt());
                channel.messageChannel(String.format("It was a great battle, but only one left standing.... %s has won %s points! %s current points: %s", sender, duel.getAmt(), sender, channel.getPointsMap().get(sender.toLowerCase())));
            } else {
                channel.addPoints(duel.getChallenger(), duel.getAmt());
                channel.removePoints(sender, duel.getAmt());
                channel.messageChannel(String.format("It was a great battle, but only one left standing.... %s has won %s points! %s current points: %s", duel.getChallenger(), duel.getAmt(), duel.getChallenger(), channel.getPointsMap().get(duel.getChallenger().toLowerCase())));
            }

            duel.cancel();
            channel.getDuels().remove(duel);
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }
}
