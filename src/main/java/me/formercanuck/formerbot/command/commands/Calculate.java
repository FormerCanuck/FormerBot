package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Bot;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;

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
            if (args.size() < 3) {
                bot.messageChannel(String.format("%s to use !calc you must have a number then an operator followed by another number. ex:1+2", sender));
                return;
            }

            double num1 = Double.parseDouble(args.get(0));
            String operator = args.get(1);
            double num2 = Double.parseDouble(args.get(2));


            bot.messageChannel(String.format("%s, %s %s %s = %s", sender, num1, operator, num2, calculate(num1, operator, num2)));
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
