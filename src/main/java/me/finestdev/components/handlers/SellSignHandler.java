package me.finestdev.components.handlers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.PlayerAPI;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.finestdev.components.utils.Utils;

public class SellSignHandler implements Listener {

    public SellSignHandler() {
        Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
    }

    @EventHandler
    public void signChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[Sell]")) {
            try {
                if (!blockIsValidEcoSign(new String[] { event.getLine(0), event.getLine(1), event.getLine(2), event.getLine(3) })) {
                    event.setLine(0, ChatColor.DARK_RED + event.getLine(0));
                    event.getPlayer().sendMessage(ChatColor.RED + "Sign format invalid");
                    return;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                event.setLine(0, ChatColor.DARK_RED + event.getLine(0));
                event.getPlayer().sendMessage(ChatColor.RED + "Sign format invalid");
                return;
            }

            if (event.getPlayer().hasPermission("create")) {
                event.setLine(0, ChatColor.DARK_BLUE + event.getLine(0));
            } else {
                event.setLine(0, ChatColor.DARK_RED + event.getLine(0));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.hasBlock() && event.getClickedBlock().getState() instanceof Sign && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Sign s = (Sign) event.getClickedBlock().getState();
            if (blockIsValidEcoSign(s)) {
                if (s.getLine(0).matches(ChatColor.DARK_BLUE + "[Sell]")) {
                    Integer amt = Integer.valueOf(s.getLine(1));
                    Integer s2 = 0;
                    ItemStack i = nameToItemStack(s.getLine(2), amt);
                    if (!p.getInventory().containsAtLeast(i, amt)) {
                        p.sendMessage(Utils.chat("&4&lSHOP&r&7 You don't have enough items in your inventory."));
                        return;
                    }
                    for (ItemStack item : p.getInventory().getContents()) {
                        if (item != null && item.isSimilar(i) && s2 < amt) {
                            p.getInventory().removeItem(item);
                            s2 = s2 + item.getAmount();
                        }
                    }
                    p.updateInventory();
                    if (s2 > amt) {
                        s2 = s2 - amt;
                        p.getInventory().addItem(new ItemStack(i.getTypeId(), s2, i.getDurability()));
                        p.updateInventory();
                    }
                    Session session = PlayerAPI.getSession(event.getPlayer());
                    session.awardTokens((int) Math.round(Double.valueOf(s.getLine(3).replace("$", ""))), true);
                    // Components.getEcon().depositPlayer(p.getName(),
                    // Double.valueOf(s.getLine(3).replace("$", "")));
                    p.sendMessage(Utils.chat("&4&lSHOP&r&7 Item sold"));
                    p.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        if (event.getBlock() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).matches(ChatColor.DARK_BLUE + "[Sell]")) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getBlockAgainst().getState() instanceof Sign && blockIsValidEcoSign((Sign) event.getBlockAgainst().getState())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof Sign)) {
            return;
        }
        Sign s = (Sign) event.getBlock().getState();
        if (blockIsValidEcoSign(s)) {
            Session session = PlayerAPI.getSession(event.getPlayer());
            if (session.getRank().getId() < Rank.ADMIN.getId()) {
                event.setCancelled(true);
            }
        }
    }

    public static Map<String, String> namesToIds = new HashMap<>();

    public boolean hasItem(String name) {
        if (namesToIds.containsKey(name.toLowerCase())) {
            return true;
        }
        int numMatches = 0;
        for (String key : namesToIds.keySet()) {
            if (key.startsWith(name.toLowerCase())) {
                numMatches++;
            }
        }
        return numMatches >= 1;
    }

    @SuppressWarnings("deprecation")
    public ItemStack nameToItemStack(String name, Integer amt) {
        name = name.toLowerCase();
        if (name.contains(":")) {
            try {
                return new ItemStack(Integer.valueOf(name.split(":")[0]), amt, Short.valueOf(name.split(":")[1]));
            } catch (NumberFormatException e) {
                if (!namesToIds.containsKey(name.split(":")[0])) {
                    for (String key : namesToIds.keySet()) {
                        if (key.startsWith(name)) {
                            return new ItemStack(Integer.valueOf(namesToIds.get(key).split(":")[0]), amt,
                                    Short.valueOf(namesToIds.get(key).split(":")[1]));
                        }
                    }

                    return new ItemStack(Material.matchMaterial(name.split(":")[0]), amt,
                            Short.valueOf(name.split(":")[1]));
                } else {
                    String s2 = name.split(":")[0];
                    String s3 = name.split(":")[1];
                    return new ItemStack(Integer.valueOf(namesToIds.get(s2).split(":")[0]), amt, Short.valueOf(s3));
                }
            }
        } else {
            try {
                return new ItemStack(Integer.valueOf(name), amt);
            } catch (NumberFormatException e) {
                if (!namesToIds.containsKey(name.toLowerCase())) {
                    for (String key : namesToIds.keySet()) {
                        if (key.startsWith(name)) {
                            return new ItemStack(Integer.valueOf(namesToIds.get(key).split(":")[0]), amt,
                                    Short.valueOf(namesToIds.get(key).split(":")[1]));
                        }
                    }

                    return new ItemStack(Material.matchMaterial(name), amt);
                } else {
                    return new ItemStack(Integer.valueOf(namesToIds.get(name).split(":")[0]), amt,
                            Short.valueOf(namesToIds.get(name).split(":")[1]));
                }
            }
        }
    }

    public boolean blockIsValidEcoSign(String[] lines) {
        try {
            Integer.valueOf(lines[1]);
            String s2 = lines[2];
            String s3 = s2;
            if (s2.contains(":")) {
                s3 = s2.split(":")[0];
            }
            try {
                Integer.valueOf(s3);
            } catch (NumberFormatException e) {
                if (!namesToIds.containsKey(s3.toLowerCase())) {
                    boolean b = false;
                    for (String key : namesToIds.keySet()) {
                        if (key.startsWith(s3)) {
                            b = true;
                        }
                    }

                    if (Material.matchMaterial(s3) == null && !b) {
                        return false;
                    }
                }
            }
            Double.valueOf(lines[3].replace("$", ""));
        } catch (Exception e) {
            return false;
        }
        return lines[0].matches(ChatColor.DARK_BLUE + "[Sell]");
    }

    public boolean blockIsValidEcoSign(Sign sign) {
        return blockIsValidEcoSign(new String[] { sign.getLine(0), sign.getLine(1), sign.getLine(2), sign.getLine(3) });
    }

}
