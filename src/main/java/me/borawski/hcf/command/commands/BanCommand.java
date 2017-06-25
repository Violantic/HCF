package me.borawski.hcf.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.punishment.Punishment.Type;
import me.borawski.hcf.punishment.PunishmentHandler;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.DateUtils;
import me.borawski.hcf.util.PlayerUtils;

/**
 * Created by Ethan on 3/8/2017.
 */
public class BanCommand extends CustomCommand {

    public BanCommand() {
        super("ban", "Ban a user from the server.", Rank.MODERATOR);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "/ban <user>");
            return;
        }

        if (!PlayerUtils.hasPlayed(args[0])) {
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "This player has never logged in before and cannot be banned!");
            return;
        }

        Session user = sender instanceof Player ? SessionHandler.getSession((Player) sender) : null;
        Session target = SessionHandler.getSession(PlayerUtils.getUUIDFromName(args[0]));

        if (user == null || target.getRank().getId() >= user.getRank().getId()) {
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You may only ban people below your rank!");
            return;
        }

        long time;
        try {
            time = DateUtils.parseDateDiff(args[1], true);
        } catch (Exception ex) {
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "Invalid ban time!");
            return;
        }

        StringBuilder reason = new StringBuilder(args[3]);
        for (int i = 3; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        long finalTime = System.currentTimeMillis() + time;

        Punishment punishment = new Punishment();
        punishment.setPunished(target.getUniqueId());
        punishment.setIssued(System.currentTimeMillis());
        punishment.setExpirationTime(finalTime);
        punishment.setReason(reason.toString().trim());
        punishment.setIssuer(user != null ? user.getUniqueId() : Core.getConsoleUUID());
        punishment.setType(Type.BAN);
        PunishmentHandler.getInstance().save(punishment);

        sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You " + (finalTime == -1 ? "permanently" : "temporarily") + " banned " + ChatColor.YELLOW + args[0] + ChatColor.GRAY + " for " + ChatColor.YELLOW + reason + ChatColor.GRAY + "!");
    }

}
