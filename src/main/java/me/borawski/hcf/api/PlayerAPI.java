package me.borawski.hcf.api;

import me.borawski.hcf.Core;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.PlayerUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Ethan on 3/8/2017.
 */
public class PlayerAPI {

    public static Session getSession(UUID uuid) {
        return Session.getSession(uuid);
    }

    public static Session getSession(String name) {
        return Session.getSession(PlayerUtils.getUUIDFromName(name));
    }

    public static Session getSession(Player player) {
        return Session.getSession(player);
    }

    public Rank getRank(Player player) {
        return getSession(player).getRank();
    }

    public Rank getRank(UUID uuid) {
        return getSession(uuid).getRank();
    }

    public Rank getRank(String name) {
        return getSession(name).getRank();
    }

    public boolean hasPlayed(String name) {
        return PlayerUtils.hasPlayed(name);
    }

    public static void issueTempConsolePunishment(Punishment.Type type, UUID uuid, long until, String reason) {
        Core.getInstance().getPunishmentManager().issue(type, uuid, new UUID(0, 16), until, reason);
    }

    public static void issuePermaConsolePunishment(Punishment.Type type, UUID uuid, String reason) {
        Core.getInstance().getPunishmentManager().issue(type, uuid, new UUID(0, 16), -1, reason);
    }

}
