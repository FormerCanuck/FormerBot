package me.formercanuck.formerbot.files;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class TodoFile {

    private Yaml yaml;
    private File file;

    private String fileName = "todo";

    private LinkedHashMap<String, Object> config;

    private Logger logger = Logger.getLogger("TODO FILE");

    public TodoFile() {
        String dataFolder = System.getProperty("user.home") + "\\Local Settings\\";
        File parentFolderFile = new File(dataFolder + "FormerB0t");
        if (!parentFolderFile.exists()) {
            if (parentFolderFile.mkdir()) {
                logger.info("[ConfigFile]: Created the main dir.");
            } else {
                logger.severe("[ConfigFile]: Could not create the main dir.");
            }
        }

        file = new File(parentFolderFile, fileName + ".yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    logger.info("[ConfigFile]: Created the config file: " + fileName);
                } else {
                    logger.severe("[ConfigFile]: Could not create the config file: " + fileName);
                }
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        }

        yaml = new Yaml();

        try {
            InputStream inputStream = new FileInputStream(file);
            config = yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            logger.severe(e.getMessage());
        }

        if (config == null) {
            config = new LinkedHashMap<>();
        }
    }

    private void save() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        dumperOptions.setPrettyFlow(true);
        yaml = new Yaml(dumperOptions);

        try {
            FileWriter writer = new FileWriter(file);
            yaml.dump(config, writer);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public boolean contains(String key) {
        return config.containsKey(key);
    }

    public void remove(String key) {
        config.remove(key);
        save();
    }

    public void set(String key, Object value) {
        config.put(key, value);
        save();
    }

    public Object get(String key) {
        return config.get(key);
    }

    public void addTODO(String todo) {
        config.put(String.valueOf(config.keySet().size() + 1), todo);
        save();
    }
}
