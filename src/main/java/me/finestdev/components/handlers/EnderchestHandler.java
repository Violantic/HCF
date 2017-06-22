package me.finestdev.components.handlers;

import me.borawski.hcf.backend.api.PlayerAPI;
import me.borawski.hcf.backend.session.Session;
import me.finestdev.components.MscAchievements;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.finestdev.components.Components;
import me.finestdev.components.utils.Utils;

public class EnderchestHandler implements Listener {

    public EnderchestHandler() {
        Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                if (Utils.enderchestDisabled == true) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("EnderChest are disabled!");
                } else {
                    Session s = PlayerAPI.getSession(event.getPlayer());
                    if (!s.hasAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN.getId())) {
                        s.awardAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN, true);
                    }

                    event.setCancelled(false);
                }
            }
        }
    }
}
