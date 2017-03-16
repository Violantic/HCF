package me.borawski.hcf.frontend.util;

import org.bukkit.ChatColor;

import java.util.Date;

/**
 * Created by Ethan on 3/12/2017.
 */
public class KickUtil {

    public static String getKick(Reason reason) {
        String primary = (ChatColor.RED + "" + ChatColor.BOLD);
        return primary + "Your account has been kicked!\n\n" +
                "Reason: " + ChatColor.WHITE + reason.name() + primary + "\n";
    }

    public enum Reason {
        DATA_CHANGE,
        MANUAL,
        ERROR,
        WARNING,
    }

}
