package me.borawski.hcf.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.mongodb.morphia.dao.BasicDAO;

import me.borawski.hcf.Core;
import me.borawski.hcf.punishment.PunishmentHandler;

public class SessionHandler extends BasicDAO<Session, Integer> {

    private static SessionHandler instance;

    private Session console;

    private List<Session> sessions;

    public SessionHandler() {
        super(Session.class, Core.getInstance().getMongoWrapper().getDatastore());

        // create a dummy console session
        console = new Session();
        console.setRank(Rank.OWNER);

        sessions = new LinkedList<>();
    }

    public static void initialize() {
        instance = new SessionHandler();
    }
    
    /**
     * Gets the session of a user and initializes it if it does not yet exist.
     * 
     * @param o
     * @return
     */
    public static Session getSession(Object o) {
        Session session;
        if (o instanceof OfflinePlayer || o instanceof UUID) {
            for (Session s : instance.sessions) {
                if (s.getUniqueId().equals(o instanceof OfflinePlayer ? ((OfflinePlayer) o).getUniqueId() : o)) {
                    return s;
                }
            }
            session = initializeSession(o, false);
            session.setActivePunishments(PunishmentHandler.getInstance().createQuery().field("punished").equal(session.getUniqueId()).field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis())).asList());
        } else if (o instanceof ConsoleCommandSender) {
            session = instance.console;
        } else {
            session = null;
        }
        return session;
    }

    public static Session initializeSession(Object o, boolean cache) {
        Session session = instance.findOne("uuid", o instanceof OfflinePlayer ? ((OfflinePlayer) o).getUniqueId() : o);
        if (session == null) {
            session = createSession(o);
        }
        if (cache) {
            instance.sessions.add(session);
        }
        return session;
    }

    private static Session createSession(Object o) {
        OfflinePlayer op;
        if (o instanceof OfflinePlayer) {
            op = (OfflinePlayer) o;
        } else if (o instanceof UUID) {
            op = Bukkit.getOfflinePlayer((UUID) o);
        } else {
            return null;
        }
        if (op == null) {
            return null;
        }

        Session session = new Session();
        session.setUniqueId(op.getUniqueId());
        session.setName(op.getName());
        session.setRank(Rank.GUEST);
        session.setTokens(0);
        session.setLevel(1);
        session.setExp(0);
        session.setFirstLogin(System.currentTimeMillis());
        session.setLastLogin(System.currentTimeMillis());
        session.setTotalPlayed(0);
        session.setIp("10.0.0.1");
        session.setAchievements(new ArrayList<>());
        session.setFriends(new ArrayList<>());
        session.setSettings(new HashMap<>());

        instance.save(session);

        return session;
    }

    public static SessionHandler getInstance() {
        return instance;
    }

}
