package me.borawski.hcf.command.commands.rank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.PlayerUtils;

public class RankSetCommand extends CustomCommand {

    public RankSetCommand() {
        super("set", "Sets a user's rank.", Rank.ADMIN);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length == 2) {
            String name = args[0];
            String rank = args[1];
            Session s = SessionHandler.getSession(PlayerUtils.getUUIDFromName(name));
            if (s == null) {
                System.out.println("[Core] [ERROR] : Could not retrieve " + name);
                return;
            }
            if (Rank.valueOf(rank) == null) {
                System.out.println("Invalid rank");
                return;
            }
            s.setRank(Rank.valueOf(rank));
            SessionHandler.getInstance().save(s);
            sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF " + ChatColor.RESET + "" + ChatColor.GRAY + "You have set " + ChatColor.YELLOW + name + "" + ChatColor.GRAY + "'s rank to " + ChatColor.YELLOW + rank);

            if (Bukkit.getPlayer(s.getUniqueId()) != null) {
                PlayerUtils.setPrefix(s.getRank().getPrefix(), Bukkit.getPlayer(s.getUniqueId()));
                s.sendMessage(ChatColor.GREEN + "You are now a " + s.getRank().name().toUpperCase());
            }
        }
        // TODO add usage message
    }

}
