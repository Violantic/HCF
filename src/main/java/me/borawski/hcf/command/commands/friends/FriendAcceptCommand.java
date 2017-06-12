package me.borawski.hcf.command.commands.friends;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.ChatUtils;
import me.borawski.hcf.util.FriendUtils;
import me.borawski.hcf.util.PlayerUtils;

public class FriendAcceptCommand extends CustomCommand {

    public FriendAcceptCommand() {
        super("accept", "Accept a friend request.", Rank.GUEST, "confirm");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Core.getLangHandler().getString("only-players"));
            return;
        }
        if (args.length == 0) {
            if (!PlayerUtils.hasPlayed(args[0])) {
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "Player not found!");
                return;
            }
            Session session = Session.getSession((Player) sender);
            Session target = Session.getSession(PlayerUtils.getUUIDFromName(args[0]));
            if (FriendUtils.isFriends(session, target.getUUID())) {
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You're already friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");
                return;
            }

            if (!FriendUtils.hasRequest(session, target.getUUID())) {
                // Sender has not already sent a friend request to this
                // player. //
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You don't have a request from " + ChatUtils.getNameWithRankColor(target.getUUID(), false));
                return;
            }

            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You accepted a request from " + ChatUtils.getNameWithRankColor(target.getUUID(), false));
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You are now friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");

            FriendUtils.acceptFriendRequest(session, target.getUUID(), true);
            FriendUtils.acceptFriendRequest(target, session.getUUID(), false);
        } else {
            sender.sendMessage(Core.getLangHandler().getString("usage-header") + "§7/friend " + label + " [player]");
        }
    }

}
