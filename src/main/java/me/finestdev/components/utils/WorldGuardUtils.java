package me.finestdev.components.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardUtils {

    private static WorldGuardPlugin worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

    public static WorldGuardPlugin getWorldGuard() {
        return WorldGuardUtils.worldGuard;
    }

    public static boolean isPvPEnabled(final Player player) {
        Location location = player.getLocation();
        return worldGuard.getRegionManager(location.getWorld()).getApplicableRegions(BukkitUtil.toVector(location)).allows(DefaultFlag.PVP);
    }

    public static ProtectedRegion getProtectedRegion(final Location location) {
        for (final ProtectedRegion protectedRegion : worldGuard.getRegionManager(location.getWorld()).getApplicableRegions(location)) {
            if (protectedRegion != null) {
                return protectedRegion;
            }
        }
        return null;
    }

}
