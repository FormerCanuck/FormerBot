package me.formercanuck.formerbot.command.commands;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.files.ConfigFile;

import java.util.ArrayList;
import java.util.List;

public class Whitelist extends Command {

    ConfigFile config;

    private List<String> whitelised;

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public void onCommand(String sender, String channel, ArrayList<String> args) {
        config = new ConfigFile(channel.substring(1) + "Whitelist");
        if (config.getWhitelist() != null) whitelised = config.getWhitelist();
        else whitelised = new ArrayList<>();

        System.out.println(Main.getInstance().getBot().isMod(sender) + " " + args);

        if (Main.getInstance().getBot().isMod(sender)) {
            if (args.size() > 0) {
                if (args.get(0).equalsIgnoreCase("add")) {
                    if (!whitelised.contains(args.get(1))) {
                        whitelised.add(args.get(1));
                        config.set("whitelist", whitelised);
                        Main.getInstance().getBot().messageChannel(String.format("%s, successfully added %s to the whitelist", sender, args.get(1)));
                        return;
                    } else {
                        Main.getInstance().getBot().messageChannel(String.format("%s, %s is already whtelisted.", sender, args.get(1)));
                        return;
                    }
                } else if (args.get(0).equalsIgnoreCase("remove")) {
                    if (whitelised.contains(args.get(1))) {
                        whitelised.remove(args.get(1));
                        config.set("whitelist", whitelised);
                        Main.getInstance().getBot().messageChannel(String.format("%s, successfully removed %s from the whitelist", sender, args.get(1)));
                        return;
                    } else {
                        Main.getInstance().getBot().messageChannel(String.format("%s, %s isn't on the whtelisted.", sender, args.get(1)));
                        return;
                    }
                }
            }
        }
    }
}
