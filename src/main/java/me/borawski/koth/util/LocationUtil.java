package me.borawski.koth.util;

import me.borawski.hcf.Core;
import org.bukkit.Location;

/**
 * Created by Ethan on 4/26/2017.
 */
public class LocationUtil {

    public static Location getLocation(String name) {
        return new Location(Core.getInstance().getServer().getWorld("world"), Double.valueOf(name.split(",")[0]), Double.valueOf(name.split(",")[1]), Double.valueOf(name.split(",")[2]), 0, 0);
    }

}
