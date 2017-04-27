package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.util.TimeUtil;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.koth.Koth;
import me.borawski.koth.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

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
        Location l = Plugin.getInternal().getKothManager().getScheduledKoth().get(Plugin.KOTH_ID).getCenter();
        Koth k = Plugin.getInternal().getKothManager().getScheduledKoth().get(Plugin.KOTH_ID);
        sender.sendMessage(Plugin.PREFIX + "Current KOTH: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + Plugin.getInternal().getKothManager().getScheduledKoth().get(Plugin.KOTH_ID).getName());
        sender.sendMessage(Plugin.PREFIX + "Location: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "X: " + l.getBlockX() + ", Y: " + l.getBlockY() + ", Z: " + l.getBlockZ());
        sender.sendMessage(Plugin.PREFIX + "Players: " + ChatColor.YELLOW + "" + ChatColor.BOLD + Plugin.getInternal().getKothManager().getScheduledKoth().get(Plugin.KOTH_ID).getPlayers().size());
        sender.sendMessage(Plugin.PREFIX + "Time: " + ChatColor.YELLOW + "" + ChatColor.BOLD + TimeUtil.getTime((System.currentTimeMillis()+(1000*k.length()))-(System.currentTimeMillis())));
    }
}
