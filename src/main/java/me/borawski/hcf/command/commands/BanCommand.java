package me.borawski.hcf.command.commands;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.Command;
import me.borawski.hcf.connection.Mongo;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.PlayerUtils;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ethan on 3/8/2017.
 */
public class BanCommand implements Command {

    private final Core instance;
    private final PrettyTime timeFormatter;

    public BanCommand(Core instance) {
        this.instance = instance;
        this.timeFormatter = new PrettyTime();
    }

    public Core getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public Rank requiredRank() {
        return Rank.MODERATOR;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if(!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "/ban <user>");
            return;
        }

        if (!PlayerUtils.hasPlayed(args[0])) {
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "This player has never logged in before and cannot be banned!");
            return;
        }

        Session target = Session.getSession(PlayerUtils.getUUIDFromName(args[0]));

        if (target.getRank().getId() >= Session.getSession(player).getRank().getId()) {
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You may only ban people below your rank!");
            return;
        }

        long time;
        try {
            time = Long.parseLong(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "Invalid ban time!");
            return;
        }

        TimeUnit timeUnit = TimeUnit.DAYS;

        StringBuilder reason = new StringBuilder(args[3]);
        for (int i = 3; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        long finalTime = System.currentTimeMillis() + (timeUnit.toMillis(time));

        Document document = new Document("uuid", target.getUUID().toString());
        document.append("start", System.currentTimeMillis());
        document.append("end", finalTime);
        document.append("reason", reason.toString());
        document.append("issuer", player.getUniqueId().toString());
        document.append("type", "BAN");
        Mongo.getCollection("punishments").insertOne(document);

        player.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You " + (finalTime==-1?"permanently":"temporarily") + " banned " + ChatColor.YELLOW + args[0] + ChatColor.GRAY + " for " + ChatColor.YELLOW + reason + ChatColor.GRAY + "!");
    }


}
