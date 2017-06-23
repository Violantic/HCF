package me.borawski.hcf.punishment;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.mongodb.morphia.dao.BasicDAO;

import me.borawski.hcf.Core;
import me.borawski.hcf.punishment.Punishment.Type;

/**
 * Created by Ethan on 3/12/2017.
 */
public class PunishmentHandler extends BasicDAO<Punishment, Integer> {

    private static PunishmentHandler instance;

    public PunishmentHandler() {
        super(Punishment.class, Core.getInstance().getMongoWrapper().getDatastore());
        instance = this;
    }

    public void issuePunishment(Type type, UUID punished, UUID issuer, long time, String reason) {
        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(type);
        punishment.setPunished(punished);
        punishment.setIssuer(issuer);
        punishment.setExpirationTime(time + System.currentTimeMillis());
        punishment.setReason(reason);
        save(punishment);

        if (Bukkit.getPlayer(punished) != null) {
            if (type == Punishment.Type.BAN) {
                Bukkit.getPlayer(punished).kickPlayer(Core.getInstance().getPrefix() + "\n&7You have been &eBANNED&7!".replace("&", ChatColor.COLOR_CHAR + ""));
            } else if (type == Punishment.Type.MUTE) {
                Bukkit.getPlayer(punished).sendMessage(Core.getInstance().getPrefix() + "You have been &eMUTED&7!".replace("&", ChatColor.COLOR_CHAR + ""));
            }
        }
    }

    public Punishment getPunishment(UUID uuid) {
        Punishment punishment = findOne("uuid", uuid);
        if (punishment != null) {
            System.out.println("[DesireHCF] Found punishment document for: " + uuid);
            System.out.println("[DesireHCF] Punishment Type: " + punishment.getReason());
            return punishment;
        }

        System.out.println("[DesireHCF] Punishment document was not found for: " + uuid);
        return null;
    }

    public static PunishmentHandler getInstance() {
        return instance;
    }

}
