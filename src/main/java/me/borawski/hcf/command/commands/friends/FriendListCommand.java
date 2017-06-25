package me.borawski.hcf.command.commands.friends;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.ChatUtils;

public class FriendListCommand extends CustomBaseCommand {

    public FriendListCommand() {
        super("list", "List all of your friends", Rank.GUEST, "show");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Core.getLangHandler().getString("only-players"));
            return;
        }
        CustomCommand sub;
        if (args.length == 0) {
            Session session = SessionHandler.getSession((Player) sender);
            if (session.getFriends().size() == 0) {
                sender.sendMessage(Core.getInstance().getPrefix() + "You have no friends");
                return;
            }
            sender.sendMessage(ChatColor.DARK_GRAY + "-------------------" + Core.getInstance().getPrefix().replace(" ", "") + ChatColor.DARK_GRAY + "-----------------------");
            for (UUID uuid : session.getFriends()) {
                String status = Core.getInstance().getServer().getPlayer(uuid) == null ? ChatColor.RED + "[OFFLINE]" : ChatColor.GREEN + "[ONLINE]";
                sender.sendMessage(ChatUtils.getNameWithRankColor(uuid, true) + " " + ChatColor.DARK_GRAY + "- " + status);
            }
            sender.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
            return;
        } else if ((sub = getSubCommand(args[0])) == null) {
            help(sender, label);
        } else {
            Session s = sender instanceof Player ? SessionHandler.getSession(((Player) sender).getUniqueId()) : null;
            if (s == null || s.getRank().getId() >= requiredRank.getId()) {
                sub.run(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
            } else {
                sender.sendMessage(Core.getLangHandler().getString("no-permissions"));
            }
        }
    }

}
