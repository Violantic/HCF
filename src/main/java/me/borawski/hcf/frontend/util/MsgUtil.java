package me.borawski.hcf.frontend.util;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.P;
import com.massivecraft.factions.listeners.FactionsPlayerListener;
import me.borawski.hcf.backend.session.Session;
import me.borawski.koth.Plugin;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import static org.bukkit.ChatColor.*;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Ethan on 4/30/2017.
 */
public class MsgUtil {

    public static void sendMessage(Player player, String...msg) {
        new FancyMessage(msg[0]);
    }

    public static void handleChat(String msg, Player player) {
        Bukkit.getOnlinePlayers().stream().forEach(new Consumer<Player>() {
            @Override
            public void accept(Player players) {
                Session s = Session.getSession(player);
                Faction f = Factions.getInstance().getByTag(FactionsPlayerListener.factions.get(player.getUniqueId()));

                if(f == null) {
                    new FancyMessage(s.getRank().getPrefix() + " " + player.getName() + ": " + s.getRank().getColor() + msg).tooltip(new String[]{
                            ChatColor.RED + "User is not in a faction"
                    }).send(players);
                    return;
                }

                new FancyMessage(s.getRank().getPrefix() + " " + player.getName() + ": " + s.getRank().getColor() + msg).tooltip(new String[]{
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "FACTION INFO",
                        ChatColor.GRAY + "Name: " + ChatColor.YELLOW + "" + f.getTag(),
                        ChatColor.GRAY + "Members: " + ChatColor.YELLOW + "" + f.getFPlayers().size(),
                        ChatColor.GRAY + "Trophy Points: " + ChatColor.YELLOW + "" + Plugin.getInternal().getFactionSession().getScore(f.getTag())
                }).send(players);
            }
        });
    }

}
