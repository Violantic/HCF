package me.borawski.hcf.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * @author Michael Ziluck
 *
 */
public abstract class CustomCommand {

    protected List<CustomCommand> subCommands;

    protected String name;

    protected String description;

    protected String permission;

    protected String[] aliases;

    /**
     * @param name
     * @param description
     * @param permission
     * @param aliases
     */
    public CustomCommand(String name, String description, String permission, String... aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
        this.subCommands = new ArrayList<>();
    }

    /**
     * Execute the command with the proper parameter.
     * 
     * @param sender
     * @param label
     * @param args
     */
    public abstract void run(CommandSender sender, String label, String[] args);

    /**
     * @return the name of the command.
     */
    public String getName() {
        return name;
    }

    /**
     * @return a description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the permission required to use the command.
     */
    protected String getPermission() {
        return permission;
    }

    /**
     * @return the aliases of the command.
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Checks whether the passed in command string matches this particular
     * custom command.
     * 
     * @param command
     * @return whether the parameter matches the command.
     */
    public boolean matches(String command) {
        if (command == null) {
            return false;
        }
        if (command.equalsIgnoreCase(name)) {
            return true;
        }
        for (String alias : aliases) {
            if (command.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a new subcommand.
     * 
     * @param subCommand
     */
    public void addSubCommand(CustomCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    /**
     * @param cmd
     * @return the subcommand of the given name. Null if it does not exist.
     */
    public CustomCommand getSubCommand(String cmd) {
        for (CustomCommand command : this.subCommands) {
            if (command.matches(cmd)) {
                return command;
            }
        }
        return null;
    }

    protected String getSubCommandString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < subCommands.size(); i++) {
            sb.append(subCommands.get(i).getName());
            if (i != subCommands.size() - 1) {
                sb.append('/');
            }
        }
        sb.append(']');
        return sb.toString();
    }

}