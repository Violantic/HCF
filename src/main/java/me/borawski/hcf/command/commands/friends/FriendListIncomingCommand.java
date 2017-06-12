package me.borawski.hcf.command.commands.friends;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.ChatUtils;

public class FriendListIncomingCommand extends CustomCommand {

    public FriendListIncomingCommand() {
        super("incoming", "Lists incoming friend requests.", Rank.GUEST);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        Session session = Session.getSession((Player) sender);
        if (session.getIncomingRequests().size() == 0) {
            sender.sendMessage(Core.getInstance().getPrefix() + "You have no incoming requests.");
            return;
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "-------------------" + Core.getInstance().getPrefix().replace(" ", "") + ChatColor.DARK_GRAY + "-----------------------");
        for (UUID uuid : session.getIncomingRequests()) {
            sender.sendMessage(ChatColor.GRAY + "Request from: " + ChatUtils.getNameWithRankColor(uuid, true));
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        return;
    }

}
