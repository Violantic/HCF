package me.borawski.hcf.command.commands.friends;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.ChatUtils;

public class FriendListOutgoingCommand extends CustomCommand {

    public FriendListOutgoingCommand() {
        super("outgoing", "Lists incoming friend requests.", Rank.GUEST);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        Session session = SessionHandler.getSession((Player) sender);
        if (session.getIncomingFriendRequests().size() == 0) {
            sender.sendMessage(Core.getInstance().getPrefix() + "You have no outgoing requests.");
            return;
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "-------------------" + Core.getInstance().getPrefix().replace(" ", "") + ChatColor.DARK_GRAY + "-----------------------");
        for (UUID uuid : session.getOutgoingFriendRequests()) {
            sender.sendMessage(ChatColor.GRAY + "Request sent to: " + ChatUtils.getNameWithRankColor(uuid, true));
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        return;
    }

}
