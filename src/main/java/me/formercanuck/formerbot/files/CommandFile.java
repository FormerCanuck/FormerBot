package me.formercanuck.formerbot.files;

import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.commands.CustomCommand;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommandFile {

    private File file;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private List<String> commandFileContents = new ArrayList<>();
    private List<String> loadedCommands = new ArrayList<>();

    public CommandFile(String channel) {
        String dataFolder = System.getProperty("user.home") + "\\Local Settings\\FormerB0t\\" + channel + "\\";
        File parent = new File(dataFolder);
        if (!parent.exists()) parent.mkdir();

        File file = new File(dataFolder + "commands.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            loadCustomCommands();
            bufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomCommand(CustomCommand command) {
        String temp = command.getName() + " " + command.getCooldown() + " " + command.getResponse();
        if (!commandFileContents.contains(temp) || !loadedCommands.contains(temp)) {
            commandFileContents.add(temp);
        }
        save();
    }

    public void removeCustomCommand(CustomCommand command) {
        String temp = command.getName() + " " + command.getCooldown() + " " + command.getResponse();
        commandFileContents.remove(temp);
        loadedCommands.remove(temp);
        save();
    }

    public void save() {
        StringBuilder str = new StringBuilder();

        for (String s : loadedCommands) {
            if (!commandFileContents.contains(s)) {
                commandFileContents.add(s);
            }
        }

        loadedCommands.clear();

        for (String s : commandFileContents) {
            str.append(s).append("\n");
        }
        try {
            bufferedWriter.write(str.toString());
            bufferedWriter.flush();
            commandFileContents.clear();
            loadedCommands.clear();
            loadCustomCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCustomCommands() {
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] args = line.split(" ");
                String name = args[0];
                int cooldown = Integer.parseInt(args[1]);

                StringBuilder response = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    response.append(args[i]).append(" ");
                }

                CustomCommand temp = new CustomCommand(name, cooldown, response.toString().trim());
                if (Main.getInstance().getBot().getCommandManager().getCustomCommand(name) == null) {
                    Main.getInstance().getBot().getCommandManager().addCustomCommand(temp);
                    loadedCommands.add(line);
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
