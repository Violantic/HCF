package me.borawski.hcf.frontend.listener;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.backend.util.PlayerUtils;
import me.borawski.hcf.backend.util.TimeUtil;
import me.borawski.hcf.frontend.util.ChatUtils;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.UUID;

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
        if (session.isBanned()) {
            Document document = Mongo.getCollection("punishments").find(new Document("uuid", event.getPlayer().getUniqueId().toString())).first();
            assert document != null;

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED
                    , (getInstance().getPrefix() + "\n" +
                    "\n" +
                    "&c&lYou are banned from the network!\n" +
                    "\n" +
                    "&cReason: &7{reason}\n" +
                    "&cUntil: &7{until}\n" +
                    "&cBanned By: &7{issuer}\n" +
                    "\n" +
                    "&7Visit &ehttps://desirehcf.net/rules&7 for our terms and rules")
                            .replace("{reason}", document.getString("reason"))
                            .replace("{until}", TimeUtil.getTime(document.getLong("end")))
                            .replace("{issuer}", PlayerUtils.getName(UUID.fromString(document.getString("issuer"))))
                            .replace("&", ChatColor.COLOR_CHAR + ""));
            return;
        }

        new BukkitRunnable() {
            public void run() {
                event.getPlayer().setPlayerListName(session.getRank().getPrefix() + " " + ChatColor.GRAY + event.getPlayer().getName());
            }
        }.runTaskLaterAsynchronously(getInstance(), 5L);
    }

    @EventHandler
    public void logout(PlayerQuitEvent event) {
        Session.getSession(event.getPlayer()).dump();
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        Session s = Session.getSession(event.getPlayer());
        if (s.isMuted()) {
            event.setCancelled(true);
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            s.sendMessage("");
            ChatUtils.sendCenteredMessage(event.getPlayer(), getInstance().getPrefix().replace(" ", ""));
            s.sendMessage("");
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "You are muted and " + ChatColor.RED + "CANNOT " + ChatColor.GRAY + "speak!");
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "Visit our rules @ " + ChatColor.YELLOW + "https://desirehcf.net/rules");
            s.sendMessage("");
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
        }
        event.setFormat(s.getRank().getPrefix() + " " + event.getPlayer().getName() + ": " + s.getRank().getColor() + event.getMessage());
    }
}
