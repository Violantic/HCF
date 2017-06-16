package me.borawski.hcf.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.MPlayer;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.session.FSession;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;

/**
 * Created by Ethan on 5/7/2017.
 */
public class FStatCommand extends CustomBaseCommand {

    public FStatCommand() {
        super("fstat", "View your player stats", Rank.GUEST, "fstats");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Core.getLangHandler().getString("only-players"));
                return;
            }
            String faction = MPlayer.get(sender).getFactionName().replace(Factions.NAME_NONE_DEFAULT, "None");
            FSession session = FSession.getSession(faction);
            sender.sendMessage(getPrefix() + "Faction: " + ChatColor.YELLOW + faction);
            sender.sendMessage(getPrefix() + "Trophy Points: " + ChatColor.YELLOW + session.getTrophies());
            sender.sendMessage(getPrefix() + "KOTH Wins: " + ChatColor.YELLOW + session.getKoth());
        } else {
            Session s = Session.getSession((Player) sender);
            if (s.getRank().getId() >= Rank.MODERATOR.getId()) {
                String faction = args[0];
                try {
                    FSession session = FSession.getSession(faction);
                    sender.sendMessage(getPrefix() + "Faction: " + ChatColor.YELLOW + session.getName());
                    sender.sendMessage(getPrefix() + "Trophy Points: " + ChatColor.YELLOW + session.getTrophies());
                    sender.sendMessage(getPrefix() + "KOTH Captures: " + ChatColor.YELLOW + session.getKoth());
                } catch (Exception e) {
                    sender.sendMessage(getPrefix() + "Could not find season data for " + ChatColor.YELLOW + faction);
                }
            } else {
                sender.sendMessage(Core.getLangHandler().getString("no-permissions"));
            }
        }
    }

    private String getPrefix() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "FSTATS " + ChatColor.RESET + "" + ChatColor.GRAY;
    }
}
