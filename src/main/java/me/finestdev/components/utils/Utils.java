package me.finestdev.components.utils;

import me.finestdev.components.Components;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Utils {

    public static boolean enderchestDisabled = Components.getInstance().getConfig().getBoolean("enderchest-disabled");

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String toString(Location l) {
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public static Location toLocation(String s) {
        String[] sp = s.split(",");
        return new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]) + .5, Double.parseDouble(sp[2]), Double.parseDouble(sp[3]) + .5);
    }

    public static String noPerms() {
        return chat("&4&lPERMISSIONS&r&7 You don't have permission for that!");
    }

}
