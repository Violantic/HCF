package me.borawski.hcf.command;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;

/**
 * @author Michael Ziluck
 *
 */
public abstract class CustomBaseCommand extends CustomCommand {

    /**
     * @param name
     * @param description
     * @param permission
     * @param aliases
     */
    public CustomBaseCommand(String name, String description, String permission, String... aliases) {
        super(name, description, permission, aliases);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        CustomCommand sub;
        if (args.length == 0 || (sub = getSubCommand(args[0])) == null) {
            help(sender, label);
        } else if (sender.hasPermission(sub.getPermission())) {
            sub.run(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendMessage(Core.getLangHandler().getString("no-permissions"));
        }
    }

    /**
     * Sends the help content to the player.
     * 
     * @param sender
     * @param label
     */
    public void help(CommandSender sender, String label) {
        sender.sendMessage(Core.getLangHandler().getString("command-list-header"));
        for (CustomCommand command : subCommands) {
            sender.sendMessage(" b/" + label + " " + command.getName() + ": 7" + command.getDescription());
        }
    }

}