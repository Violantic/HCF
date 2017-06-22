package me.borawski.hcf.util;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.listeners.FactionsPlayerListener;
import me.borawski.hcf.session.FSession;
import me.borawski.hcf.session.Session;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

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
                Session s = Session.getSession(player);
                Faction f = Factions.getInstance().getByTag(FactionsPlayerListener.factions.get(player.getUniqueId()));
                FSession fSession = FSession.getSession(f.getTag());

                if (fSession == null) {
                    new FancyMessage(s.getRank().getPrefix() + " " + player.getName() + ": " + s.getRank().getColor() + msg).tooltip(new String[] {
                            ChatColor.RED + "User is not in a faction"
                    }).send(players);
                    return;
                }

                new FancyMessage(s.getRank().getPrefix() + " " + player.getName() + ": " + s.getRank().getColor() + msg).tooltip(new String[] {
                        ChatColor.DARK_RED + "" + ChatColor.BOLD + "FACTION INFO",
                        ChatColor.GRAY + "Name: " + ChatColor.YELLOW + "" + fSession.getName(),
                        ChatColor.GRAY + "Members: " + ChatColor.YELLOW + "" + f.getFPlayers().size(),
                        ChatColor.GRAY + "Trophy Points: " + ChatColor.YELLOW + "" + fSession.getTrophies()
                }).send(players);
            }
        });
    }

}
