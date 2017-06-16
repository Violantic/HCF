package me.borawski.hcf.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.KickUtil;
import me.borawski.hcf.util.PlayerUtils;

/**
 * Created by Ethan on 3/12/2017.
 */
public class RankCommand extends CustomBaseCommand {


    public RankCommand() {
        super("rank", "View your rank or manage others.", Rank.ADMIN);
    }

   
    @SuppressWarnings("deprecation")
    public void run2(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            Session s = null;
            try {
                s = Session.getSession(((Player) sender).getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF " + ChatColor.RESET + "" + ChatColor.GRAY + "Your rank is currently " + ChatColor.YELLOW + s.getRank().getDisplayName());
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                String name = args[1];
                String rank = args[2];
                Session s = Session.getSession(PlayerUtils.getUUIDFromName(name));
                if (s == null) {
                    System.out.println("[Core] [ERROR] : Could not retrieve " + name);
                    return;
                }
                if (Rank.valueOf(rank) == null) {
                    System.out.println("Invalid rank");
                    return;
                }
                s.updateDocument("players", "rank", rank);
                sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF " + ChatColor.RESET + "" + ChatColor.GRAY + "You have set " + ChatColor.YELLOW + name + "" + ChatColor.GRAY + "'s rank to " + ChatColor.YELLOW + rank);

                if (Bukkit.getPlayer(name) != null) {
                    Bukkit.getPlayer(name).kickPlayer(KickUtil.getKick(KickUtil.Reason.DATA_CHANGE));
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid arguments /rank [set <user>]");
        }
    }
}
