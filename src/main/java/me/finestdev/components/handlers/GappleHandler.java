package me.finestdev.components.handlers;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import me.borawski.hcf.backend.api.PlayerAPI;
import me.borawski.hcf.backend.session.Session;
import me.finestdev.components.MscAchievements;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import me.finestdev.components.Components;
import me.finestdev.components.utils.Cooldown;
import me.finestdev.components.utils.Cooldown.CooldownBase;
import me.finestdev.components.utils.Cooldown.Time;
import me.finestdev.components.utils.Utils;

public class GappleHandler implements Listener {

    private final Cooldown cooldown;

    public GappleHandler() {
        Bukkit.getPluginManager().registerEvents(this, Components.getInstance());

        (cooldown = Components.getComponents().getCooldown(Components.GAPPLE)).setOnEndSequece(new Consumer<UUID>() {

            @Override
            public void accept(UUID id) {
                Bukkit.getScheduler().runTask(Components.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(id).sendMessage(Utils.chat(Components.getInstance().getConfig().getString("gapple_ended")));
                    }
                });
            }
        });
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() == 1) {
            CooldownBase base = cooldown.get(p.getUniqueId());
            if (base == null || Cooldown.getAmountLeft(base) <= 0) {
                Session s = PlayerAPI.getSession(e.getPlayer());
                if (!s.hasAchievement("first_gapple")) {
                    s.awardAchievement(MscAchievements.FIRST_GAPPLE, true);
                }
                cooldown.startCooldown(p.getUniqueId(), Cooldown.timeToMillis(Components.getInstance().getConfig().getString("gapple_time")));
            } else {
                e.setCancelled(true);
                String message = Components.getInstance().getConfig().getString("gapple_message");
                long left = Cooldown.getAmountLeft(base);
                Map<Time, Long> times = Cooldown.timeFromMillis(left);
                message = message.replace("<days>", (times.containsKey(Time.DAY) ? times.get(Time.DAY) : 0) + "d");
                message = message.replace("<hours>",
                        (times.containsKey(Time.HOUR) ? times.get(Time.HOUR) : 0) + "h");
                message = message.replace("<minutes>",
                        (times.containsKey(Time.MINUTE) ? times.get(Time.MINUTE) : 0) + "m");
                message = message.replace("<seconds>",
                        (times.containsKey(Time.SECOND) ? times.get(Time.SECOND) : 0) + "s");
                p.sendMessage(Utils.chat(message));
            }
        }
    }

}
