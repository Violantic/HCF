package me.borawski.hcf.command.commands;

import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.TimeUtil;
import me.borawski.hcf.command.Command;
import me.borawski.koth.Koth;
import me.borawski.koth.KothManager;
import me.borawski.koth.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 4/26/2017.
 */
public class KothCommand implements Command {

    @Override
    public String getName() {
        return "koth";
    }

    @Override
    public Rank requiredRank() {
        return Rank.GUEST;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Koth k = Plugin.getInternal().getCurrentKoth();
        Location l = k.getCenter();
        sender.sendMessage(Plugin.PREFIX + "Current KOTH: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + Plugin.getInternal().getCurrentKoth().getName());
        sender.sendMessage(Plugin.PREFIX + "Location: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "X: " + l.getBlockX() + ", Y: " + l.getBlockY() + ", Z: " + l.getBlockZ());
        sender.sendMessage(Plugin.PREFIX + "Players: " + ChatColor.YELLOW + "" + ChatColor.BOLD + Plugin.getInternal().getPlayersAttending().size());
        sender.sendMessage(Plugin.PREFIX + "Ends: " + ChatColor.YELLOW + "" + ChatColor.BOLD + TimeUtil.getTime((System.currentTimeMillis()+(1000*k.length()))-(System.currentTimeMillis())));

        Session s = Session.getSession(((Player) sender).getUniqueId());
        if(args.length == 1) {
            if(s.getRank().getId() >= Rank.ADMIN.getId()) {
                if(args[0].equalsIgnoreCase("pause")) {
                    if(!KothManager.paused) {
                        KothManager.paused = true;
                        sender.sendMessage(Plugin.PREFIX + "You have paused the current Koth");
                    } else {
                        KothManager.paused = false;
                        sender.sendMessage(Plugin.PREFIX + "You have resumed the current Koth");
                    }
                } else if(args[0].equalsIgnoreCase("finish")) {
                    KothManager.paused = true;
                    Plugin.second.set(1199);
                    sender.sendMessage(Plugin.PREFIX + "Ending current Koth, starting the next");
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "PERMISSIONS " + ChatColor.RESET + "" + ChatColor.GRAY + "You don't have permission to view faction stats!");
            }
        }
    }
}
