package me.finestdev.components.handlers;

import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.finestdev.components.Components;
import me.finestdev.components.utils.Cooldown;
import me.finestdev.components.utils.Cooldown.CooldownBase;
import me.finestdev.components.utils.Utils;

public class PvPTimer implements CommandExecutor, Listener {

    private final Cooldown cooldown;
    private Components instance;

    public PvPTimer(Components instance) {
        this.instance = instance;
        Bukkit.getPluginManager().registerEvents(this, getInstance().getPlugin());
        getInstance().getPlugin().getCommand("pvptimer").setExecutor(this);

        (cooldown = getInstance().getCooldown(Components.PVPT)).setOnEndSequece(new Consumer<UUID>() {

            @Override
            public void accept(UUID id) {
                Bukkit.getScheduler().runTask(getInstance().getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(id).sendMessage(Utils.chat(/*
                                                                     * COnfigurable
                                                                     * Ended
                                                                     * Message
                                                                     */""));
                    }
                });
            }
        });
    }

    public Components getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only for players!");
            return true;
        }
        Player p = (Player) sender;

        if (p.hasPermission("hcf.pvptimer")) {
            if (args.length == 0) {
                p.sendMessage(Utils.chat(getInstance().getPlugin().getConfig().getString("pvptimer_message")));
                return true;
            } else if (args.length == 1) {
                CooldownBase base = cooldown.get(p.getUniqueId());
                if (args[0].equalsIgnoreCase("disable")) {
                    if (base == null) {
                        p.sendMessage(Utils.chat(getInstance().getPlugin().getConfig().getString("pvptimer_active_message")));
                        return true;
                    }
                    if (Cooldown.getAmountLeft(base) <= 0) {
                        p.sendMessage(Utils.chat(getInstance().getPlugin().getConfig().getString("pvptimer_disabled")));
                        cooldown.endCooldown(p.getUniqueId());
                        return true;
                    }
                }
                return true;
            } else if (args.length > 1) {
                p.sendMessage("Too many arguments!");
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onRespawnEvent(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        CooldownBase base = cooldown.get(p.getUniqueId());
        if (base == null || Cooldown.getAmountLeft(base) <= 0) {
            cooldown.startCooldown(p.getUniqueId(),
                    Cooldown.timeToMillis(getInstance().getPlugin().getConfig().getString("pvptimer_time")));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity p = e.getEntity();
        Entity a = e.getDamager();
        CooldownBase base = cooldown.get(p.getUniqueId());

        if (base != null) {
            if (Cooldown.getAmountLeft(base) > 0) {
                e.setCancelled(true);
                if (e.getDamager() instanceof Player) {
                    a.sendMessage(Utils
                            .chat(getInstance().getPlugin().getConfig().getString("pvptimer_attacker_message").replace("<player>", p.getName())));
                }
            }
        }
    }
}
