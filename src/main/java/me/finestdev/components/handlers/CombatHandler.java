package me.finestdev.components.handlers;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

import me.borawski.hcf.backend.api.PlayerAPI;
import me.borawski.hcf.backend.session.Session;
import me.finestdev.components.MscAchievements;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.finestdev.components.Components;
import me.finestdev.components.utils.Cooldown;
import me.finestdev.components.utils.Utils;

public class CombatHandler implements Listener {

    public static ArrayList<UUID> tagged = new ArrayList<UUID>();

    private final Cooldown cooldown;

    public CombatHandler() {
        Bukkit.getPluginManager().registerEvents(this, Components.getInstance());

        (cooldown = Components.getComponents().getCooldown(Components.CBTLOG)).setOnEndSequece(new Consumer<UUID>() {

            @Override
            public void accept(UUID id) {
                Bukkit.getScheduler().runTask(Components.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        tagged.remove(id);
                        try {
                            System.out.println(id.toString() + " has been removed from combat");
                            Bukkit.getPlayer(id).sendMessage(Utils.chat(Components.getInstance().getConfig().getString("combattag_removed_message")));
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        });
    }

    @EventHandler
    public void onEntityDmgByEntity(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player)) {
            return;
        }

        Entity victim = e.getEntity();
        Player attacker = (Player) e.getDamager();

        if (attacker != victim) {
            if (!tagged.contains(victim.getUniqueId())) {
                Session s = PlayerAPI.getSession(attacker);
                if (!s.hasAchievement("first_combat")) {
                    s.awardAchievement(MscAchievements.FIRST_COMBAT, true);
                }
                cooldown.startCooldown(victim.getUniqueId(),
                        Cooldown.timeToMillis(Components.getInstance().getConfig().getString("combattag_time")));
                tagged.add(victim.getUniqueId());
                victim.sendMessage(Utils
                        .chat(Components.getInstance().getConfig().getString("combattag_victim_message").replace("<attacker>", attacker.getName())));
            } else {
                cooldown.startCooldown(victim.getUniqueId(),
                        Cooldown.timeToMillis(Components.getInstance().getConfig().getString("combattag_time")));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (tagged.contains(p.getUniqueId())) {
            LivingEntity npc = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), EntityType.PLAYER);
            npc.setCustomName(Utils.chat("&f" + p.getName()));
            tagged.remove(p.getUniqueId());
            cooldown.endCooldown(p.getUniqueId());
        }
    }
}
