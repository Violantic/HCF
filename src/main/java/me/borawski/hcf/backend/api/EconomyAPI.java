package me.borawski.hcf.backend.api;

import me.borawski.hcf.backend.session.Session;

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
    }

    public static void giveTokens(UUID uuid, int amount, boolean inform) {
        Session session = Session.getSession(uuid);
        setTokens(uuid, session.getTokens()+amount);
    }

    public static void setTokens(UUID uuid, int amount) {
        Session session = Session.getSession(uuid);
        session.updateDocument("players", "tokens", amount);
    }

}
