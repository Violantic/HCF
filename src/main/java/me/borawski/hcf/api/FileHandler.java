package me.borawski.hcf.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Michael Ziluck
 *
 */
public class FileHandler {

    protected FileConfiguration file;

    protected HashMap<String, Object> history;

    /**
     * Construct a new optimized file handler.
     * 
     * @param file
     */
    public FileHandler(FileConfiguration file) {
        history = new HashMap<>();
        this.file = file;
    }

    /**
     * Reloads the {@link ConfigHandler}. This resets the file and clears the
     * history object.
     * 
     * @param file
     */
    public void reload(FileConfiguration file) {
        this.file = file;
        history.clear();
    }

    /**
     * Gets a formatted string from the config file. Replaces any color
     * placeholders as well. If the string does not exist in the config, returns
     * null.
     * 
     * @param key
     * 
     * @return the formatted string.
     */
    public String getString(String key) {
        String message = null;
        Object o = history.get(key);
        if (o != null && o instanceof String) {
            return (String) o;
        }
        message = file.getString(key);
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', file.getString(key));
            history.put(key, message);
            return message;
        } else {
            return key;
        }
    }

    /**
     * Gets a double value from history or the config. If it does not exist,
     * returns 0.
     * 
     * @param key
     * 
     * @return the value.
     */
    public double getDouble(String key) {
        double value;
        Object o = history.get(key);
        if (o != null && o instanceof Double) {
            return (Double) o;
        }
        value = file.getDouble(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a integer value from history or the config. If it does not exist,
     * returns 0.
     * 
     * @param key
     * 
     * @return the value.
     */
    public int getInteger(String key) {
        int value;
        Object o = history.get(key);
        if (o != null && o instanceof Integer) {
            return (Integer) o;
        }
        value = file.getInt(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a boolean value from history or the config. If it does not exist,
     * returns 0.
     * 
     * @param key
     * 
     * @return the value.
     */
    public boolean getBoolean(String key) {
        boolean value;
        Object o = history.get(key);
        if (o != null && o instanceof Integer) {
            return (Boolean) o;
        }
        value = file.getBoolean(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a formatted string list from the config file. Replaces any color
     * placeholders as well. If the string list does not exist in the config,
     * returns null.
     * 
     * @param key
     * 
     * @return the formatted string list.
     */
    @SuppressWarnings("unchecked")
    public List<String> getStringList(String key) {
        Object o = history.get(key);
        if (o != null && o instanceof List<?>) {
            return (List<String>) o;
        }
        List<String> list = new LinkedList<>();
        for (String str : file.getStringList(key)) {
            list.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        if (list.size() == 0) {
            list.add(getString(key));
        }
        return list;
    }

}