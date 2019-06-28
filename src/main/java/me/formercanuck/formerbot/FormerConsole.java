package me.formercanuck.formerbot;

import me.fc.console.Console;
import me.fc.console.command.Command;

import java.awt.*;

public class FormerConsole extends Console {

    public FormerConsole(String name) {
        super(name);

        getCommandManager().addCommand(new Command() {
            @Override
            public void onCommand(String[] args) {
                if (args.length < 2) {
                    println(getUse(), Color.RED);
                } else {
                    String channel = args[1];
                    Main.getInstance().getBot().joinChannel(channel);
                }
            }

            @Override
            public String getName() {
                return "join";
            }

            @Override
            public String getUse() {
                return "/join <channel name>";
            }
        });
    }
}
