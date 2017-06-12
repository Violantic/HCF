package me.borawski.hcf.command;

import java.util.LinkedList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;

/**
 * @author Michael Ziluck
 *
 */
public class CustomCommandHandler implements CommandExecutor {

    private LinkedList<CustomCommand> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CustomCommand command = getCustomCommand(label);
        if (command != null) {
            if (sender.hasPermission(command.getPermission())) {
                command.run(sender, label, args);
            } else {
                sender.sendMessage(Core.getLangHandler().getString("no-permissions"));
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * @param command
     */
    public void registerCommand(CustomCommand command) {
        if (commands == null) {
            commands = new LinkedList<>();
        }
        commands.add(command);
    }

    private CustomCommand getCustomCommand(String cmd) {
        for (CustomCommand command : commands) {
            if (command.matches(cmd)) {
                return command;
            }
        }
        return null;
    }

}