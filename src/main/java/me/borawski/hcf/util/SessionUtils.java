package me.borawski.hcf.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

public class SessionUtils {

    public static Rank getRank(Object o) {
        OfflinePlayer op;
        if (o instanceof Player) {
            op = (Player) o;
        } else if (o instanceof UUID) {
            op = Bukkit.getOfflinePlayer((UUID) o);
        } else if (o instanceof ConsoleCommandSender) {
            return Rank.OWNER;
        } else {
            return null;
        }
        Session s = SessionHandler.getSession(op);
        return s == null ? null : s.getRank();
    }

}
