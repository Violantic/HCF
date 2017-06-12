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

public class FriendRemoveCommand extends CustomCommand {

    public FriendRemoveCommand() {
        super("remove", "Remove a friend.", Rank.GUEST, "unfriend", "delete");
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
            if (!FriendUtils.isFriends(session, target.getUUID())) {
                if (FriendUtils.hasRequest(session, target.getUUID())) {
                    // Deny the request for the sender and receiver //
                    FriendUtils.denyFriendRequest(session, target.getUUID(), true);
                    FriendUtils.denyFriendRequest(target, session.getUUID(), false);
                    sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You weren't friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + ", but you had a friend request from them so we denied it");
                    return;
                }
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You aren't friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false));
                return;
            }

            FriendUtils.removeFriend(session, target.getUUID());
            FriendUtils.removeFriend(target, session.getUUID());
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You removed " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + " from your friend list!");
        }
    }

}
