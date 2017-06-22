package me.borawski.hcf.punishment;

import me.borawski.hcf.Core;
import me.borawski.hcf.connection.Mongo;
import me.borawski.hcf.session.Session;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ethan on 3/12/2017.
 */
public class PunishmentManager {

    private Core instance;

    public PunishmentManager(Core instance) {
        this.instance = instance;
    }

    public Core getInstance() {
        return instance;
    }

    public void issue(Punishment.Type type, UUID player, UUID issuer, long until, String reason) {
        long time = until;
        TimeUnit timeUnit = TimeUnit.DAYS;
        long finalTime = System.currentTimeMillis() + (timeUnit.toMillis(time));
        Document document = new Document("uuid", player.toString());
        document.append("start", System.currentTimeMillis());
        document.append("end", finalTime);
        document.append("reason", reason);
        document.append("issuer", issuer.toString());
        document.append("type", type.name());
        Mongo.getCollection("punishments").insertOne(document);
        Session.getSession(player).dump();
        if (Bukkit.getPlayer(player) != null) {
            if (type == Punishment.Type.BAN) {
                Bukkit.getPlayer(player).kickPlayer(getInstance().getPrefix() + "\n&7You have been &eBANNED&7!".replace("&", ChatColor.COLOR_CHAR + ""));
            } else if (type == Punishment.Type.MUTE) {
                Bukkit.getPlayer(player).sendMessage(getInstance().getPrefix() + "You have been &eMUTED&7!".replace("&", ChatColor.COLOR_CHAR + ""));
            }
        }
    }

    public Punishment getPunishment(UUID player) {
        Document document = Mongo.getCollection("punishments").find(new Document("uuid", player.toString())).first();
        if (document != null) {
            System.out.println("[DesireHCF] Found punishment document for: " + player.toString());
            System.out.println("[DesireHCF] Punishment Type: " + document.getString("type"));
            return new Punishment(player, Punishment.Type.valueOf(document.getString("type")), document);
        } else {
            System.out.println("[DesireHCF] Punishment document was not found for: " + player.toString());
        }

        return null;
    }

}
