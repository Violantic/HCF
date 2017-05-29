package com.massivecraft.factions;

import com.massivecraft.factions.zcore.persist.json.JSONFPlayers;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FPlayers {
    public static Map<UUID, String> factionCache = new ConcurrentHashMap<UUID, String>();

    protected static com.massivecraft.factions.FPlayers instance = getFPlayersImpl();

    public abstract void clean();

    public static com.massivecraft.factions.FPlayers getInstance() {
        return instance;
    }

    private static com.massivecraft.factions.FPlayers getFPlayersImpl() {
        switch (Conf.backEnd) {
            case JSON:
                return new JSONFPlayers();
        }
        return null;
    }

    public abstract Collection<FPlayer> getOnlinePlayers();

    public abstract FPlayer getByPlayer(Player player);

    public abstract Collection<FPlayer> getAllFPlayers();

    public abstract void forceSave();

    public abstract void forceSave(boolean sync);

    public abstract FPlayer getByOfflinePlayer(OfflinePlayer player);

    public abstract FPlayer getById(String string);

    public abstract void load();
}
