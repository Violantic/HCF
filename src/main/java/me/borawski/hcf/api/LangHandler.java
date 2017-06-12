package me.borawski.hcf.api;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Michael Ziluck
 *
 */
public class LangHandler extends FileHandler {

    private String prefix;

    /**
     * Create a new {@link LangHandler} based on the {@link FileHandler}. Also
     * loads the prefix.
     * 
     * @param file
     */
    public LangHandler(FileConfiguration file) {
        super(file);
        prefix = super.getString("prefix");
    }

    /**
     * Gets a formatted string from the config file. Replaces any color place
     * holders as well. If the string does not exist in the config, returns
     * null.
     * 
     * @param string
     * @return the formatted string.
     */
    public String getString(String string) {
        return prefix + " §r" + super.getString(string);
    }

}