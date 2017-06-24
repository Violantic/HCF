package me.borawski.hcf.command.commands.rank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

public class RankCheckCommand extends CustomCommand {

    public RankCheckCommand() {
        super("check", "Check your rank.", Rank.GUEST);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Core.getLangHandler().getString("only-players"));
            return;
        }

        Session s = null;
        try {
            s = SessionHandler.getSession(((Player) sender).getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF " + ChatColor.RESET + "" + ChatColor.GRAY + "Your rank is currently " + ChatColor.YELLOW + s.getRank().getDisplayName());
    }

}
