package me.borawski.hcf.frontend.command.commands;

import com.massivecraft.factions.listeners.FactionsPlayerListener;
import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.FSession;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.koth.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 5/7/2017.
 */
public class FStatCommand implements Command {

    @Override
    public String getName() {
        return "fstat";
    }

    @Override
    public Rank requiredRank() {
        return Rank.GUEST;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            try {
                String faction = FactionsPlayerListener.factions.get(((Player) sender).getUniqueId());
                FSession session = FSession.getSession(faction);
                int score = session.getTrophies();
                int koth = session.getKoth();
                sender.sendMessage(getPrefix() + "Faction: " + ChatColor.YELLOW + faction);
                sender.sendMessage(getPrefix() + "Trophy Points: " + ChatColor.YELLOW + score);
                sender.sendMessage(getPrefix() + "KOTH Wins: " + ChatColor.YELLOW + koth);
            } catch (Exception e) {
                sender.sendMessage(getPrefix() + "We couldn't find your faction stats! (Maybe you're not in a faction?)");
            }
        } else if(args.length == 1) {
            Session s = Session.getSession((Player) sender);
            if(s.getRank().getId() >= Rank.MODERATOR.getId()) {
                String faction = args[0];
                try {
                    int score = Plugin.getInternal().getFactionSession().getScore(faction);
                    int koth = Plugin.getInternal().getFactionSession().getKothWins(faction);
                    int event = Plugin.getInternal().getFactionSession().getEventWins(faction);
                    sender.sendMessage(getPrefix() + "Faction: " + ChatColor.YELLOW + faction);
                    sender.sendMessage(getPrefix() + "Trophy Points: " + ChatColor.YELLOW + score);
                    sender.sendMessage(getPrefix() + "KOTH Wins: " + ChatColor.YELLOW + koth);
                    sender.sendMessage(getPrefix() + "Event Wins: " + ChatColor.YELLOW + event);
                } catch (Exception e) {
                    sender.sendMessage(getPrefix() + "Could not find season data for " + ChatColor.YELLOW + faction);
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "PERMISSIONS " + ChatColor.RESET + "" + ChatColor.GRAY + "You don't have permission to view faction stats!");
            }
        } else if(args.length == 3) {
            Session s = Session.getSession((Player) sender);
            if(s.getRank().getId() >= Rank.ADMIN.getId()) {
                String faction = args[2];
                try {
                    String usage = "";
                    if(args[0].equalsIgnoreCase("setpoints")) {
                        Integer points = Integer.valueOf(args[1]);
                        Plugin.getInternal().getFactionSession().setScore(faction, "points", points);
                        usage = "Trophy Points";
                    } else if(args[0].equalsIgnoreCase("setkothwins")) {
                        Integer koth = Integer.valueOf(args[1]);
                        Plugin.getInternal().getFactionSession().setScore(faction, "koth_wins", koth);
                        usage = "KOTH Wins";
                    } else if(args[0].equalsIgnoreCase("seteventwins")) {
                        Integer event = Integer.valueOf(args[1]);
                        Plugin.getInternal().getFactionSession().setScore(faction, "event_wins", event);
                        usage = "Event Wins";
                    }
                    sender.sendMessage(getPrefix() + "You have updated " + ChatColor.YELLOW + usage + "" + ChatColor.GRAY + " for " + ChatColor.YELLOW + faction);
                } catch (Exception e) {
                    sender.sendMessage(getPrefix() + "Could not find season data for " + ChatColor.YELLOW + faction);
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "PERMISSIONS " + ChatColor.RESET + "" + ChatColor.GRAY + "You don't have permission to view faction stats!");
            }
        }
    }

    public static String getPrefix() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "FSTATS " + ChatColor.RESET + "" + ChatColor.GRAY;
    }
}
