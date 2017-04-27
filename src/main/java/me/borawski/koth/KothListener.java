package me.borawski.koth;

import me.borawski.hcf.frontend.util.BarUtil;
import me.borawski.koth.util.LangUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Ethan on 4/26/2017.
 */
public class KothListener implements Listener {

    private Plugin instance;

    public KothListener(Plugin instance) {
        this.instance = instance;
    }

    public Plugin getInstance() {
        return instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(getInstance().getCurrentKoth().getPlayers().contains(event.getEntity().getUniqueId())) {
            getInstance().getCurrentKoth().getPlayers().remove(event.getEntity().getUniqueId());
            event.getEntity().sendMessage(Plugin.PREFIX + "You have left " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + getInstance().getCurrentKoth().getName());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        boolean moved = ((event.getFrom().getBlockX() != event.getTo().getBlockX()) || (event.getFrom().getBlockY() != event.getTo().getBlockY()) || (event.getFrom().getBlockZ() != event.getTo().getBlockZ()));
        if(!moved) return;

        Location center = getInstance().getCurrentKoth().getCenter();
        double distance = center.distanceSquared(event.getTo());
        if(distance <= getInstance().getCurrentKoth().radius() && !instance.getCurrentKoth().getPlayers().contains(event.getPlayer().getUniqueId())) {
            onEnter(event.getPlayer());
        } else if(distance > getInstance().getCurrentKoth().radius() && instance.getCurrentKoth().getPlayers().contains(event.getPlayer().getUniqueId())) {
            onLeave(event.getPlayer());
        }
    }

    public void onLeave(Player player) {
        BarUtil.sendActionBar(player, Plugin.PREFIX + LangUtil.KOTH_LEAVE);
        player.sendMessage(Plugin.PREFIX + LangUtil.KOTH_LEAVE);
        instance.getCurrentKoth().getPlayers().remove(player.getUniqueId());
        player.setScoreboard(null);
    }

    public void onEnter(Player player) {
        BarUtil.sendActionBar(player, Plugin.PREFIX + LangUtil.KOTH_ENTER);
        player.sendMessage(Plugin.PREFIX + LangUtil.KOTH_ENTER);
        instance.getCurrentKoth().getPlayers().add(player.getUniqueId());
        player.setScoreboard(getInstance().getKothManager().getScoreboard());
    }
}
