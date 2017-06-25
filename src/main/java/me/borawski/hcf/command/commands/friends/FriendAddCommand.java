package me.borawski.hcf.command.commands.friends;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.ChatUtils;
import me.borawski.hcf.util.FriendUtils;
import me.borawski.hcf.util.PlayerUtils;

public class FriendAddCommand extends CustomCommand {

    public FriendAddCommand() {
        super("add", "Add a friend.", Rank.GUEST, "invite", "befriend");
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
            Session session = SessionHandler.getSession((Player) sender);
            Session target = SessionHandler.getSession(PlayerUtils.getUUIDFromName(args[0]));
            if (FriendUtils.isFriends(session, target.getUniqueId())) {
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You are already friends with " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false));
                return;
            }

            if (FriendUtils.hasRequest(target, session.getUniqueId())) {
                // Sender has already sent a friend request to this player.
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You have already sent this player a friend request");
                return;
            }

            if (FriendUtils.hasRequest(session, target.getUniqueId())) {
                // Accept the request for the sender and receiver. //
                FriendUtils.acceptFriendRequest(session, target.getUniqueId(), true);
                FriendUtils.acceptFriendRequest(target, session.getUniqueId(), false);
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You had a friend request from " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false) + ChatColor.GRAY + " so we put both of you in each others friend lists");
                return;
            }

            if (FriendUtils.hasRequest(target, session.getUniqueId())) {
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You've already sent a request to " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false) + ChatColor.GRAY + "!");
                return;
            }

            if (FriendUtils.hasRequest(session, target.getUniqueId())) {
                FriendUtils.acceptFriendRequest(session, target.getUniqueId(), true);
                FriendUtils.acceptFriendRequest(target, session.getUniqueId(), false);
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You accepted request from " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false) + ChatColor.GRAY + "!");
                return;
            }

            FriendUtils.addFriendRequest(target, session.getUniqueId(), true);
            FriendUtils.addFriendRequest(session, target.getUniqueId(), false);
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You sent a friend request to " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false) + ChatColor.GRAY + "!");
        }
    }

}
