package me.borawski.hcf.command;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.SessionUtils;

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
    public CustomBaseCommand(String name, String description, Rank requiredRank, String... aliases) {
        super(name, description, requiredRank, aliases);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        CustomCommand sub;
        if (args.length == 0 || (sub = getSubCommand(args[0])) == null) {
            help(sender, label);
        } else {
            Session s = sender instanceof Player ? SessionHandler.getSession(sender) : null;
            if (s == null || s.getRank().getId() >= requiredRank.getId()) {
                sub.run(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
            } else {
                sender.sendMessage(Core.getLangHandler().getString("no-permissions"));
            }
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
        Rank rank = SessionUtils.getRank(sender);
        for (CustomCommand command : subCommands) {
            if (command.getRequiredRank().getId() <= rank.getId()) {
                sender.sendMessage(" §b/" + label + " " + command.getName() + ": §7" + command.getDescription());
            }
        }
    }

}