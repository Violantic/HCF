package me.borawski.koth;

import com.massivecraft.factions.P;
import com.massivecraft.factions.listeners.FactionsPlayerListener;
import me.borawski.hcf.frontend.util.BarUtil;
import me.borawski.koth.util.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Ethan on 4/26/2017.
 */
public class KothListener implements Listener {

    private Plugin instance;
    private List<UUID> insideKoth;

    public KothListener(Plugin instance) {
        this.instance = instance;
        this.insideKoth = new ArrayList<>();
    }

    public Plugin getInstance() {
        return instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (getInstance().getPlayersAttending().contains(event.getEntity().getUniqueId())) {
            getInstance().getPlayersAttending().remove(event.getEntity().getUniqueId());
            event.getEntity().sendMessage(Plugin.PREFIX + "You have left " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + getInstance().getCurrentKoth().getName());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        boolean moved = ((event.getFrom().getBlockX() != event.getTo().getBlockX()) || (event.getFrom().getBlockY() != event.getTo().getBlockY()) || (event.getFrom().getBlockZ() != event.getTo().getBlockZ()));
        if (!moved) return;
        if (getInstance().getCurrentKoth() == null) return;

        Location center = getInstance().getCurrentKoth().getCenter();
        double modulus = Math.sqrt(Math.pow(center.getX() - event.getTo().getX(), 2) + Math.pow(center.getZ() - event.getTo().getZ(), 2));         //if(!getInstance().getCurrentKoth().getPlayers().contains(event.getPlayer().getUniqueId())) return;
        if (modulus <= getInstance().getCurrentKoth().radius() && insideKoth.contains(event.getPlayer().getUniqueId())) {
            // Player is inside zone. //
        } else if (modulus >= getInstance().getCurrentKoth().radius() && insideKoth.contains(event.getPlayer().getUniqueId())) {
            insideKoth.remove(event.getPlayer().getUniqueId());
            onLeave(event.getPlayer());
        } else if (modulus <= getInstance().getCurrentKoth().radius() && !insideKoth.contains(event.getPlayer().getUniqueId())) {
            insideKoth.add(event.getPlayer().getUniqueId());
            onEnter(event.getPlayer());
        }
    }

    public void onLeave(Player player) {
        BarUtil.sendActionBar(player, Plugin.PREFIX + LangUtil.KOTH_LEAVE.replace("&", ChatColor.COLOR_CHAR + "").replace("<koth>", getInstance().getCurrentKoth().getName()));
        player.sendMessage(Plugin.PREFIX + LangUtil.KOTH_LEAVE.replace("&", ChatColor.COLOR_CHAR + "").replace("<koth>", getInstance().getCurrentKoth().getName()));
        Plugin.getInternal().getPlayersAttending().remove(player.getUniqueId());

        if (FactionsPlayerListener.factions.get(player.getUniqueId()).equalsIgnoreCase(getInstance().getKothManager().getHoldingFaction())) {
            List<UUID> otherFactionMembers = new ArrayList<>();
            getInstance().getPlayersAttending().stream().forEach(new Consumer<UUID>() {
                @Override
                public void accept(UUID uuid) {
                    if(FactionsPlayerListener.factions.get(uuid).equalsIgnoreCase(FactionsPlayerListener.factions.get(player.getUniqueId()))) {
                        otherFactionMembers.add(uuid);
                    }
                }
            });

            if(otherFactionMembers.size() == 0) {
                // Next Faction in line. //
                if(getInstance().getPlayersAttending().size() == 0) {
                    getInstance().getKothManager().setHoldingFaction(null);
                    Bukkit.broadcastMessage(Plugin.PREFIX + ChatColor.GRAY + "Nobody is capturing " + ChatColor.BOLD + "" + ChatColor.YELLOW + getInstance().getCurrentKoth().getName());
                } else {
                    getInstance().getKothManager().setHoldingFaction(FactionsPlayerListener.factions.get(getInstance().getPlayersAttending().get(0)));
                    Bukkit.broadcastMessage(Plugin.PREFIX + ChatColor.YELLOW + FactionsPlayerListener.factions.get(getInstance().getPlayersAttending().get(0)) + ChatColor.GRAY + " is now capturing " + ChatColor.YELLOW + getInstance().getCurrentKoth().getName());
                }
            }
        }
    }

    public void onEnter(Player player) {
        if(getInstance().getKothManager().getHoldingFaction() == null) {
            getInstance().getKothManager().setHoldingFaction(FactionsPlayerListener.factions.get(player.getUniqueId()));
        }
        Plugin.getInternal().getPlayersAttending().add(player.getUniqueId());
        BarUtil.sendActionBar(player, Plugin.PREFIX + LangUtil.KOTH_ENTER.replace("&", ChatColor.COLOR_CHAR + "").replace("<koth>", getInstance().getCurrentKoth().getName()));
        player.sendMessage(Plugin.PREFIX + LangUtil.KOTH_ENTER.replace("&", ChatColor.COLOR_CHAR + "").replace("<koth>", getInstance().getCurrentKoth().getName()));
        player.setScoreboard(getInstance().getKothManager().getScoreboard());
    }
}
