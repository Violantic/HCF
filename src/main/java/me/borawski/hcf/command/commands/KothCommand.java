package me.borawski.hcf.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.DateUtils;
import me.borawski.koth.Koth;
import me.borawski.koth.KothManager;
import me.borawski.koth.Plugin;

/**
 * Created by Ethan on 4/26/2017.
 */
public class KothCommand extends CustomCommand {

    public KothCommand() {
        super("koth", "View information about the current KOTH.", Rank.GUEST);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        Koth k = Plugin.getInternal().getCurrentKoth();
        Location l = k.getCenter();
        sender.sendMessage(Plugin.PREFIX + "Current KOTH: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + Plugin.getInternal().getCurrentKoth().getName());
        sender.sendMessage(Plugin.PREFIX + "Location: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "X: " + l.getBlockX() + ", Y: " + l.getBlockY() + ", Z: " + l.getBlockZ());
        sender.sendMessage(Plugin.PREFIX + "Players: " + ChatColor.YELLOW + "" + ChatColor.BOLD + Plugin.getInternal().getPlayersAttending().size());
        sender.sendMessage(Plugin.PREFIX + "Ends: " + ChatColor.YELLOW + "" + ChatColor.BOLD + DateUtils.formatDateDiff((System.currentTimeMillis() + (1000 * k.length()))));

        Session s = SessionHandler.getSession(((Player) sender).getUniqueId());
        if (args.length == 1) {
            if (s.getRank().getId() >= Rank.ADMIN.getId()) {
                if (args[0].equalsIgnoreCase("pause")) {
                    if (!KothManager.paused) {
                        KothManager.paused = true;
                        sender.sendMessage(Plugin.PREFIX + "You have paused the current Koth");
                    } else {
                        KothManager.paused = false;
                        sender.sendMessage(Plugin.PREFIX + "You have resumed the current Koth");
                    }
                } else if (args[0].equalsIgnoreCase("finish")) {
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
