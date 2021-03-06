package me.formercanuck.formerbot.files;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

public class ConfigFile {

    private Yaml yaml;
    private File file;

    private LinkedHashMap<String, Object> config;

    private Logger logger = Logger.getLogger("Config File");

    public ConfigFile(String fileName) {
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

    public List<String> getWhitelist() {
        return (List<String>) config.get("whitelist");
    }

    public List<String> getWatchList() {
        return (List<String>) config.get("watchlist");
    }

    public HashMap<String, Integer> getPointsMap() {
        return (HashMap<String, Integer>) config.get("points");
    }

    public HashMap<String, String> getHashMap(String key) {
        return (HashMap<String, String>) config.get(key);
    }

    public Integer getInt(String key) {
        return (Integer) config.get(key);
    }

    public List<String> getPals() {
        if (contains("pals")) return (List<String>) get("pals");
        else return new ArrayList<>();
    }

    public void addDefault(String key, Object value) {
        if (!config.containsKey(key)) set(key, value);
    }

    public boolean addPal(String pal) {
        List<String> temp = getPals();
        if (!temp.contains(pal)) {
            temp.add(pal);
            set("pals", temp);
            return true;
        } else return false;
    }

    public boolean removePal(String pal) {
        List<String> temp = getPals();
        if (temp.contains(pal)) {
            temp.remove(pal);
            set("pals", temp);
            return true;
        } else return false;
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

    public Boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }
}
