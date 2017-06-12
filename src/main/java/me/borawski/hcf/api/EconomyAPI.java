package me.borawski.hcf.api;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Session;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Created by Ethan on 3/20/2017.
 */
public class EconomyAPI {

    public static int getTokens(UUID uuid) {
        Session session = Session.getSession(uuid);
        return session.getTokens();
    }

    public static void takeTokens(UUID uuid, int amount, boolean inform) {
        Session session = Session.getSession(uuid);
        setTokens(uuid, session.getTokens()-amount);
        if(inform) {
            session.sendMessage(Core.getInstance().getPrefix() + "Your token amount is now " + ChatColor.YELLOW + session.getTokens());
        }
    }

    public static void giveTokens(UUID uuid, int amount, boolean inform) {
        Session session = Session.getSession(uuid);
        setTokens(uuid, session.getTokens()+amount);
        if(inform) {
            session.sendMessage(Core.getInstance().getPrefix() + "You have gained " + ChatColor.YELLOW + session.getTokens() + ChatColor.GRAY + " tokens");
        }
    }

    public static void setTokens(UUID uuid, int amount) {
        Session session = Session.getSession(uuid);
        session.updateDocument("players", "tokens", amount);
    }

}
