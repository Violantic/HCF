package me.finestdev.components.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scheduler.BukkitRunnable;

import me.finestdev.components.Components;
import me.finestdev.components.utils.Cooldown;
import me.finestdev.components.utils.Cooldown.CooldownBase;
import me.finestdev.components.utils.Cooldown.Time;
import me.finestdev.components.utils.Utils;

public class DeathBanHandler implements Listener {

    private final Set<UUID> counting = new HashSet<>();
    private final static Map<UUID, Integer> lives = new HashMap<>();

    public DeathBanHandler() {
        File dataFile = new File(Components.getInstance().getDataFolder(), "lives.data");
        try {
            if (!dataFile.exists())
                dataFile.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] vals = line.split(";");
                lives.put(UUID.fromString(vals[0]), Integer.parseInt(vals[1]));
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                saveLives();
            }
        }.runTaskTimerAsynchronously(Components.getInstance(), 1200, 1200);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        Player p = e.getEntity().getPlayer();
        Player killer = e.getEntity().getKiller();
        Cooldown cooldown = Components.getComponents().getCooldown(Components.DEATHBAN);
        p.getWorld().strikeLightningEffect(p.getLocation());
        cooldown.startCooldown(p.getUniqueId(), Cooldown.timeToMillis(Components.getInstance().getConfig().getString("deathban_time")));
        if (killer != p) {
            p.kickPlayer(Utils.chat(Components.getInstance().getConfig().getString("deathban_player_message").replace("<killer>", killer.getName())
                    + (getLives(p) > 0 ? "\n\n&cLogin in 10 seconds to use a life." : "")));
        } else {
            p.kickPlayer(Utils.chat(Components.getInstance().getConfig().getString("deathban_player_message")
                    + (getLives(p) > 0 ? "\n\n&cLogin in 10 seconds to use a life." : "")));
        }
        Bukkit.broadcastMessage(Utils.chat(Components.getInstance().getConfig().getString("deathban_broadcast")
                .replace("<victim>", p.getDisplayName()).replace("<killer>", killer.getDisplayName())));
        startCounting(p);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        CooldownBase banBase = Components.getComponents().getCooldown(Components.DEATHBAN).get(p.getUniqueId());
        if (banBase == null) {
            return;
        }
        long left = Cooldown.getAmountLeft(banBase);
        if (left <= 0) {
            return;
        }
        if (acceptLife(p)) {
            Components.getComponents().getCooldown(Components.DEATHBAN).endCooldown(p.getUniqueId());
            return;
        }
        Map<Time, Long> times = Cooldown.timeFromMillis(left);
        e.setResult(Result.KICK_OTHER);
        String message = Components.getInstance().getConfig().getString("deathban_message");
        message = message.replace("<days>", (times.containsKey(Time.DAY) ? times.get(Time.DAY) : 0) + "d");
        message = message.replace("<hours>", (times.containsKey(Time.HOUR) ? times.get(Time.HOUR) : 0) + "m");
        message = message.replace("<minutes>", (times.containsKey(Time.MINUTE) ? times.get(Time.MINUTE) : 0) + "m");
        message = message.replace("<seconds>", (times.containsKey(Time.SECOND) ? times.get(Time.SECOND) : 0) + "s");
        e.setKickMessage(Utils.chat(message + (getLives(p) > 0 ? "\n\n&cLogin in 10 seconds to use a life." : "")));
        startCounting(p);
    }

    public boolean acceptLife(Player player) {
        if (counting.contains(player.getUniqueId())) {
            counting.remove(player.getUniqueId());
            takeLives(player, 1);
            return true;
        }
        return false;
    }

    public void startCounting(Player player) {
        if (getLives(player) <= 0)
            return;
        counting.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                counting.remove(player.getUniqueId());
            }
        }.runTaskLater(Components.getInstance(), 10 * 20);
    }

    public void saveLives() {
        File dataFile = new File(Components.getInstance().getDataFolder(), "lives.data");
        try {
            if (!dataFile.exists())
                dataFile.createNewFile();

            PrintWriter writer = new PrintWriter(dataFile);

            for (Map.Entry<UUID, Integer> entry : lives.entrySet())
                writer.println(entry.getKey().toString() + ";" + String.valueOf(entry.getValue()));

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setLives(Player player, int amount) {
        UUID id = player.getUniqueId();
        Integer livesLeft = lives.get(id);
        if (livesLeft == null)
            lives.put(id, amount);
        else
            lives.replace(id, Math.max(amount, 0));
    }

    public static void addLives(Player player, int amount) {
        UUID id = player.getUniqueId();
        Integer livesLeft = lives.get(id);
        if (livesLeft == null)
            lives.put(id, amount);
        else
            lives.replace(id, Math.max(livesLeft.intValue() + amount, 0));
    }

    public static void takeLives(Player player, int amount) {
        UUID id = player.getUniqueId();
        Integer livesLeft = lives.get(id);
        if (livesLeft == null)
            lives.put(id, 0);
        else
            lives.replace(id, Math.max(livesLeft.intValue() - amount, 0));
    }

    public static int getLives(Player player) {
        return lives.containsKey(player.getUniqueId()) ? lives.get(player.getUniqueId()) : 0;
    }

}