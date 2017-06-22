package me.borawski.hcf.command;

import java.util.LinkedList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Session;

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
            Session s = sender instanceof Player ? Session.getSession(((Player)sender).getUniqueId()) : null;
            if (s == null || s.getRank().getId() >= command.getRequiredRank().getId()) {
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
        Core.getInstance().getCommand(command.name).setExecutor(this);
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