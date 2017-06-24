package me.borawski.hcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.borawski.hcf.Core;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.ChatUtils;
import me.borawski.hcf.util.DateUtils;
import me.borawski.hcf.util.MsgUtil;
import me.borawski.hcf.util.PlayerUtils;

/**
 * Created by Ethan on 3/8/2017.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        Session session = SessionHandler.getSession(event.getPlayer());
        Punishment p;
        if ((p = session.isBanned()) != null) {

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, (Core.getInstance().getPrefix() + "\n" + "\n" + "&c&lYou are banned from the network!\n" + "\n" + "&cReason: &7{reason}\n" + "&cUntil: &7{until}\n" + "&cBanned By: &7{issuer}\n" + "\n" + "&7Visit &ehttps://desirehcf.net/rules&7 for our terms and rules").replace("{reason}", p.getReason())
                    .replace("{until}", DateUtils.formatDateDiff(p.getExpirationTime())).replace("{issuer}", PlayerUtils.getName(p.getIssuer()).replace("&", "§")));
            return;
        }

    }

    public void onJoin(PlayerJoinEvent event) {
        Session session = SessionHandler.getSession(event.getPlayer());
        boolean noColor = session.getRank().getId() == 1;
        boolean justColor = session.getRank().getId() == 2;
        event.getPlayer().setPlayerListName(noColor ? ChatColor.GRAY + event.getPlayer().getName() : justColor ? session.getRank().getMain() + event.getPlayer().getName() : session.getRank().getPrefix() + " " + ChatColor.GRAY + event.getPlayer().getName());
        SessionHandler.initializeSession(event.getPlayer(), true);
    }

    @EventHandler
    public void logout(PlayerQuitEvent event) {
        Session session = SessionHandler.getSession(event.getPlayer());
        session.setTotalPlayed(session.getTotalPlayed() + (System.currentTimeMillis() - session.getLastLogin()));
        session.setLastLogin(System.currentTimeMillis());
        SessionHandler.getInstance().save(session);
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Session s = SessionHandler.getSession(event.getPlayer());
        if (s.isMuted() != null) {
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            s.sendMessage("");
            ChatUtils.sendCenteredMessage(event.getPlayer(), Core.getInstance().getPrefix().replace(" ", ""));
            s.sendMessage("");
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "You are muted and " + ChatColor.RED + "CANNOT " + ChatColor.GRAY + "speak!");
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "Visit our rules @ " + ChatColor.YELLOW + "https://desirehcf.net/rules");
            s.sendMessage("");
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            return;
        }
        MsgUtil.handleChat(event.getMessage(), event.getPlayer());
    }
}
