package me.finestdev.components.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.entity.Board;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.borawski.hcf.Core;
import me.finestdev.components.Components;
import me.finestdev.components.utils.WorldGuard;

public class RegionHandler implements Listener {

    private HashMap<UUID, List<Location>> locationsWorldGuard;
    private HashMap<UUID, List<Location>> locationsFaction;

    public RegionHandler() {
        this.locationsWorldGuard = new HashMap<UUID, List<Location>>();
        this.locationsFaction = new HashMap<UUID, List<Location>>();
        Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        Location from = playerMoveEvent.getFrom();
        Location to = playerMoveEvent.getTo();
        if (CombatHandler.tagged.contains(player)) {
            for (ProtectedRegion protectedRegion2 : WorldGuard.getWorldGuard().getRegionManager(from.getWorld())
                    .getRegions().values()) {
                for (String s : Components.getInstance().getConfig().getStringList("glass-protected-regions")) {
                    if (protectedRegion2 != null && protectedRegion2.getId().equals(s)) {
                        if (protectedRegion2.contains(to.getBlockX(), to.getBlockY(), to.getBlockZ())) {
                            playerMoveEvent.setTo(from);
                        }
                        this.renderGlassWorldGuard(player, to, protectedRegion2);
                    }
                }
            }
            FLocation fLocation = new FLocation(to);
            if (Board.getInstance().getFactionAt(fLocation).isNormal()) {
                if (this.isInside(this.getValues(fLocation)[0], this.getValues(fLocation)[1], to.getBlockX())) {
                    playerMoveEvent.setTo(from);
                }
                if (this.isInside(this.getValues(fLocation)[2], this.getValues(fLocation)[3], to.getBlockZ())) {
                    playerMoveEvent.setTo(from);
                }
            }
            FLocation relative = new FLocation(player.getLocation()).getRelative(0, 1);
            FLocation relative2 = new FLocation(player.getLocation()).getRelative(0, -1);
            FLocation relative3 = new FLocation(player.getLocation()).getRelative(1, 0);
            FLocation relative4 = new FLocation(player.getLocation()).getRelative(-1, 0);
            FLocation relative5 = new FLocation(player.getLocation()).getRelative(1, 1);
            FLocation relative6 = new FLocation(player.getLocation()).getRelative(1, -1);
            FLocation relative7 = new FLocation(player.getLocation()).getRelative(-1, 1);
            FLocation relative8 = new FLocation(player.getLocation()).getRelative(-1, -1);
            if (Board.getInstance().getFactionAt(relative).isNormal()) {
                this.renderGlassFaction(player, relative);
            }
            if (Board.getInstance().getFactionAt(relative2).isNormal()) {
                this.renderGlassFaction(player, relative2);
            }
            if (Board.getInstance().getFactionAt(relative3).isNormal()) {
                this.renderGlassFaction(player, relative3);
            }
            if (Board.getInstance().getFactionAt(relative4).isNormal()) {
                this.renderGlassFaction(player, relative4);
            }
            if (Board.getInstance().getFactionAt(relative5).isNormal()) {
                this.renderGlassFaction(player, relative5);
            }
            if (Board.getInstance().getFactionAt(relative6).isNormal()) {
                this.renderGlassFaction(player, relative6);
            }
            if (Board.getInstance().getFactionAt(relative7).isNormal()) {
                this.renderGlassFaction(player, relative7);
            }
            if (Board.getInstance().getFactionAt(relative8).isNormal()) {
                this.renderGlassFaction(player, relative8);
            }
        } else {
            this.removeGlass(player);
        }
    }

    public void renderGlassWorldGuard(Player player, Location location, ProtectedRegion protectedRegion) {
        if (protectedRegion == null) {
            return;
        }
        int closest = this.closest(location.getBlockX(), protectedRegion.getMinimumPoint().getBlockX(),
                protectedRegion.getMaximumPoint().getBlockX());
        int closest2 = this.closest(location.getBlockZ(), protectedRegion.getMinimumPoint().getBlockZ(),
                protectedRegion.getMaximumPoint().getBlockZ());
        boolean b = Math.abs(location.getX() - closest) < 8.0;
        boolean b2 = Math.abs(location.getZ() - closest2) < 8.0;
        if (!b && !b2) {
            return;
        }
        ArrayList<Location> list = new ArrayList<Location>();
        if (b) {
            for (int i = -4; i < 5; ++i) {
                for (int j = -7; j < 8; ++j) {
                    if (this.isInside(protectedRegion.getMinimumPoint().getBlockZ(),
                            protectedRegion.getMaximumPoint().getBlockZ(), location.getBlockZ() + j)) {
                        Location location2 = new Location(location.getWorld(), Double.valueOf(closest),
                                Double.valueOf(location.getBlockY() + i), Double.valueOf(location.getBlockZ() + j));
                        if (!list.contains(location2) && !location2.getBlock().getType().isOccluding()) {
                            list.add(location2);
                        }
                    }
                }
            }
        }
        if (b2) {
            for (int k = -4; k < 5; ++k) {
                for (int l = -7; l < 8; ++l) {
                    if (this.isInside(protectedRegion.getMinimumPoint().getBlockX(),
                            protectedRegion.getMaximumPoint().getBlockX(), location.getBlockX() + l)) {
                        Location location3 = new Location(location.getWorld(), Double.valueOf(location.getBlockX() + l),
                                Double.valueOf(location.getBlockY() + k), Double.valueOf(closest2));
                        if (!list.contains(location3) && !location3.getBlock().getType().isOccluding()) {
                            list.add(location3);
                        }
                    }
                }
            }
        }
        this.updateWorldGuard(player, list);
    }

    public void renderGlassFaction(Player player, FLocation fLocation) {
        Location location = player.getLocation();
        int n = (int) fLocation.getX();
        int n2 = (int) fLocation.getZ();
        int n3 = n * 16;
        int n4 = n2 * 16;
        int n5 = n * 16 + 15;
        int n6 = n2 * 16 + 15;
        int closest = this.closest(location.getBlockX(), n3, n5);
        int closest2 = this.closest(location.getBlockZ(), n4, n6);
        boolean b = Math.abs(location.getX() - closest) < 6.0;
        boolean b2 = Math.abs(location.getZ() - closest2) < 6.0;
        ArrayList<Location> list = new ArrayList<Location>();
        if (b) {
            for (int i = -4; i < 5; ++i) {
                for (int j = -4; j < 5; ++j) {
                    if (this.isInside(n4, n6, location.getBlockZ() + i)) {
                        Location location2 = new Location(location.getWorld(), Double.valueOf(closest),
                                Double.valueOf(location.getBlockY() + j), Double.valueOf(location.getBlockZ() + i));
                        if (!list.contains(location2) && !location2.getBlock().getType().isOccluding()) {
                            list.add(location2);
                        }
                    }
                }
            }
        }
        if (b2) {
            for (int k = -4; k < 5; ++k) {
                for (int l = -4; l < 5; ++l) {
                    if (this.isInside(n3, n5, location.getBlockX() + k)) {
                        Location location3 = new Location(location.getWorld(), Double.valueOf(location.getBlockX() + k),
                                Double.valueOf(location.getBlockY() + l), Double.valueOf(closest2));
                        if (!list.contains(location3) && !location3.getBlock().getType().isOccluding()) {
                            list.add(location3);
                        }
                    }
                }
            }
        }
        this.updateFaction(player, list);
    }

    public int[] getValues(FLocation fLocation) {
        int n = (int) fLocation.getX();
        int n2 = (int) fLocation.getZ();
        return new int[] { n * 16, n * 16 + 15, n2 * 16, n2 * 16 + 15 };
    }

    public int closest(int n, int... array) {
        int n2 = array[0];
        for (int i = 0; i < array.length; ++i) {
            if (Math.abs(n - array[i]) < Math.abs(n - n2)) {
                n2 = array[i];
            }
        }
        return n2;
    }

    public boolean isInside(int n, int n2, int n3) {
        return Math.abs(n - n2) == Math.abs(n3 - n) + Math.abs(n3 - n2);
    }

    @SuppressWarnings("deprecation")
    public void updateWorldGuard(Player player, List<Location> list) {
        if (this.locationsWorldGuard.containsKey(player.getUniqueId())) {
            for (Location location : this.locationsWorldGuard.get(player.getUniqueId())) {
                if (!list.contains(location)) {
                    Block block = location.getBlock();
                    player.sendBlockChange(location, block.getTypeId(), block.getData());
                }
            }
            Iterator<Location> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                player.sendBlockChange(iterator2.next(), 95, (byte) 15);
            }
        } else {
            Iterator<Location> iterator3 = list.iterator();
            while (iterator3.hasNext()) {
                player.sendBlockChange(iterator3.next(), 95, (byte) 15);
            }
        }
        this.locationsWorldGuard.put(player.getUniqueId(), list);
    }

    @SuppressWarnings("deprecation")
    public void updateFaction(Player player, List<Location> list) {
        if (this.locationsFaction.containsKey(player.getUniqueId())) {
            this.locationsFaction.get(player.getUniqueId()).addAll(list);
            Iterator<Location> iterator = list.iterator();
            while (iterator.hasNext()) {
                player.sendBlockChange(iterator.next(), 95, (byte) 15);
            }
        } else {
            Iterator<Location> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                player.sendBlockChange(iterator2.next(), 95, (byte) 15);
            }
            this.locationsFaction.put(player.getUniqueId(), list);
        }
    }

    @SuppressWarnings("deprecation")
    public void removeGlass(Player player) {
        if (this.locationsWorldGuard.containsKey(player.getUniqueId())) {
            for (Location location : this.locationsWorldGuard.get(player.getUniqueId())) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getTypeId(), block.getData());
            }
            this.locationsWorldGuard.remove(player.getUniqueId());
        }
        if (this.locationsFaction.containsKey(player.getUniqueId())) {
            for (Location location2 : this.locationsFaction.get(player.getUniqueId())) {
                Block block2 = location2.getBlock();
                player.sendBlockChange(location2, block2.getTypeId(), block2.getData());
            }
            this.locationsFaction.remove(player.getUniqueId());
        }
    }
}