package me.borawski.hcf.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;

public class SessionUtils {

    public static Rank getRank(Object o) {
        Player p;
        if (o instanceof Player) {
            p = (Player) o;
        } else if (o instanceof UUID) {
            p = Bukkit.getPlayer((UUID) o);
        } else if (o instanceof ConsoleCommandSender) {
            return Rank.OWNER;
        } else {
            return null;
        }
        Session s = Session.getSession(p);
        return s == null ? null : s.getRank();
    }

}
