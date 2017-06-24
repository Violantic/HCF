package me.borawski.hcf.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

/**
 * Created by Ethan on 3/12/2017.
 */
public class PlayerUtils {

    @SuppressWarnings("deprecation")
    public static boolean hasPlayed(String name) {
        return Bukkit.getOfflinePlayer(name) != null;
    }

    public static UUID getUUIDFromName(String name) {
        if (!hasPlayed(name)) {
            return null;
        }
        Session s = SessionHandler.getInstance().findOne("name", name);
        if (s == null) {
            return null;
        }
        return s.getUniqueId();
    }

    /**
     * Get a player's name from their UUID.
     * 
     * @param uuid
     *            the uuid of the player in question.
     * @return the last seen username of the player.
     */
    public static String getName(UUID uuid) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        return op == null ? null : op.getName();
    }

    /**
     * Set's a player's name and prefix in the tab list menu on the server.
     * 
     * @param prefix
     *            the new prefix
     * @param player
     *            the player being changed
     */
    public static void setPrefix(String prefix, Player player) {
        player.setPlayerListName(prefix + player.getName());
    }

}
