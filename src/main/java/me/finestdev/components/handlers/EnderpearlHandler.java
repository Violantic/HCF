package me.finestdev.components.handlers;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.borawski.hcf.Core;
import me.finestdev.components.Components;
import me.finestdev.components.utils.Cooldown;
import me.finestdev.components.utils.Cooldown.CooldownBase;
import me.finestdev.components.utils.Cooldown.Time;
import me.finestdev.components.utils.Utils;

public class EnderpearlHandler implements Listener {

    private final Cooldown cooldown;

    public EnderpearlHandler() {
        Bukkit.getPluginManager().registerEvents(this, Core.getInstance());

        (cooldown = Components.getInstance().getCooldown(Components.ENDERP)).setOnEndSequece(new Consumer<UUID>() {

            @Override
            public void accept(UUID id) {
                Bukkit.getScheduler().runTask(Core.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(id).sendMessage(Utils.chat(Core.getInstance().getConfig().getString("enderpearl_ended")));
                    }
                });
            }
        });
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (p.getItemInHand().getType() == Material.ENDER_PEARL) {
            CooldownBase base = cooldown.get(p.getUniqueId());
            if (base == null || Cooldown.getAmountLeft(base) <= 0) {
                e.setCancelled(false);
                cooldown.startCooldown(p.getUniqueId(),
                        Cooldown.timeToMillis(Core.getInstance().getConfig().getString("enderpearl_time")));
            } else {
                e.setCancelled(true);
                String message = Core.getInstance().getConfig().getString("enderpearl_message");
                long left = Cooldown.getAmountLeft(base);
                Map<Time, Long> times = Cooldown.timeFromMillis(left);
                message = message.replace("<days>", (times.containsKey(Time.DAY) ? times.get(Time.DAY) : 0) + "d");
                message = message.replace("<hours>", (times.containsKey(Time.HOUR) ? times.get(Time.HOUR) : 0) + "h");
                message = message.replace("<minutes>", (times.containsKey(Time.MINUTE) ? times.get(Time.MINUTE) : 0) + "m");
                message = message.replace("<seconds>", (times.containsKey(Time.SECOND) ? times.get(Time.SECOND) : 0) + "s");
                p.sendMessage(Utils.chat(message));
            }
        }
    }

}
