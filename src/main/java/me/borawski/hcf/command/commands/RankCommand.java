package me.borawski.hcf.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;

/**
 * Created by Ethan on 3/12/2017.
 */
public class RankCommand extends CustomBaseCommand {

    public RankCommand() {
        super("rank", "View your rank or manage others.", Rank.ADMIN);
    }

    public void run(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            Session s = null;
            try {
                s = Session.getSession(((Player) sender).getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF " + ChatColor.RESET + "" + ChatColor.GRAY + "Your rank is currently " + ChatColor.YELLOW + s.getRank().getDisplayName());
        } else {
            super.run(sender, label, args);
        }
    }
}
