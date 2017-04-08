package me.borawski.hcf.frontend.listener;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.hcf.backend.punishment.Punishment;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.frontend.util.BarUtil;
import me.borawski.hcf.frontend.util.ChatUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * Created by Ethan on 3/8/2017.
 */
public class PlayerListener implements Listener {

    private Core instance;
    private PrettyTime timeFormatter;

    public PlayerListener(Core instance) {
        this.instance = instance;
        this.timeFormatter = new PrettyTime();
    }

    public Core getInstance() {
        return instance;
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        Session session = Session.getSession(event.getPlayer());
        new BukkitRunnable() {
            public void run() {
                getInstance().getScoreboard().apply(event.getPlayer());
            }
        }.runTaskLater(getInstance(), 1L);
    }

    @EventHandler
    public void logout(PlayerQuitEvent event) {
        Session.getSession(event.getPlayer()).dump();
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        Session s = Session.getSession(event.getPlayer());
        if(s.isMuted()) {
            event.setCancelled(true);
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            s.sendMessage("");
            ChatUtils.sendCenteredMessage(event.getPlayer(), getInstance().getPrefix().replace(" ", ""));
            String sword = "&c&l&m--&7&l&m[&7&l&m======-".replace("&", ChatColor.COLOR_CHAR + "");
            ChatUtils.sendCenteredMessage(event.getPlayer(), sword);
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "You are muted and " + ChatColor.RED + "CANNOT " + ChatColor.GRAY + "speak!");
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "Visit our rules @ " + ChatColor.YELLOW + "https://desirehcf.net/rules");
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
        }
        event.setFormat(s.getRank().getPrefix() + " " + event.getPlayer().getName() + ": " + s.getRank().getColor() + event.getMessage());
    }
}
