package me.borawski.hcf.api;

import java.util.UUID;

import org.bukkit.ChatColor;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

/**
 * Created by Ethan on 3/20/2017.
 */
public class EconomyAPI {

    public static int getTokens(UUID uuid) {
        Session session = SessionHandler.getSession(uuid);
        return session.getTokens();
    }

    public static void removeTokens(UUID uuid, int amount, boolean inform) {
        Session session = SessionHandler.getSession(uuid);
        setTokens(uuid, session.getTokens() - amount);
        if (inform) {
            session.sendMessage(Core.getInstance().getPrefix() + "Your token amount is now " + ChatColor.YELLOW + session.getTokens());
        }
    }

    public static void giveTokens(UUID uuid, int amount, boolean inform) {
        Session session = SessionHandler.getSession(uuid);
        setTokens(uuid, session.getTokens() + amount);
        if (inform) {
            session.sendMessage(Core.getInstance().getPrefix() + "You have gained " + ChatColor.YELLOW + session.getTokens() + ChatColor.GRAY + " tokens");
        }
    }

    public static void setTokens(UUID uuid, int amount) {
        Session session = SessionHandler.getSession(uuid);
        session.setTokens(amount);
        SessionHandler.getInstance().save(session);
    }

}
