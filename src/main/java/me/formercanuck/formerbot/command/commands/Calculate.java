package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.utils.MiscUtils;

import java.util.ArrayList;

public class Calculate extends Command {
    @Override
    public String getName() {
        return "calc";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        Bot bot = Main.getInstance().getBot();
        if (bot.isMod(sender.toLowerCase())) {
            if (args.size() < 1) {
                bot.messageChannel(String.format("%s to use !calc you must have a number then an operator followed by another number. ex:1+2", sender));
                return;
            }

            bot.messageChannel(String.format("%s, here is your answer: %s", sender, MiscUtils.calC(args.get(0))));
        }
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    private double calculate(double num1, String operator, double num2) {
        switch (operator) {
            case "/":
                return num1 / num2;
            case "*":
                return num1 * num2;
            case "-":
                return num1 - num2;
            case "+":
                return num1 + num2;
            default:
                return 0;
        }
    }
}
