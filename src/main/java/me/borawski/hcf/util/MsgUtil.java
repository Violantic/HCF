package me.borawski.hcf.util;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import me.borawski.hcf.session.FactionSession;
import me.borawski.hcf.session.FactionSessionHandler;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import mkremins.fanciful.FancyMessage;

/**
 * Created by Ethan on 4/30/2017.
 */
public class MsgUtil {

    public static void sendMessage(Player player, String... msg) {
        new FancyMessage(msg[0]);
    }

    public static void handleChat(String msg, Player player) {
        Bukkit.getOnlinePlayers().stream().forEach(new Consumer<Player>() {
            @Override
            public void accept(Player players) {
                Session s = SessionHandler.getSession(player);
                Faction f = MPlayer.get(player).getFaction();

                if (f == null) {
                    new FancyMessage(s.getRank().getPrefix() + " " + player.getName() + ": " + s.getRank().getColor() + msg).tooltip(new String[] {
                            ChatColor.RED + "User is not in a faction"
                    }).send(players);
                    return;
                }

                FactionSession fSession = FactionSessionHandler.getFactionSession(f.getName());

                new FancyMessage(s.getRank().getPrefix() + " " + player.getName() + ": " + s.getRank().getColor() + msg).tooltip(new String[] {
                        ChatColor.DARK_RED + "" + ChatColor.BOLD + "FACTION INFO",
                        ChatColor.GRAY + "Name: " + ChatColor.YELLOW + "" + fSession.getName(),
                        ChatColor.GRAY + "Members: " + ChatColor.YELLOW + "" + f.getMPlayers().size(),
                        ChatColor.GRAY + "Trophy Points: " + ChatColor.YELLOW + "" + fSession.getTrophies()
                }).send(players);
            }
        });
    }

}
