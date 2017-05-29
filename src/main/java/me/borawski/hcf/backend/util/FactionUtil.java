package me.borawski.hcf.backend.util;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.zcore.persist.MemoryFPlayers;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Created by Ethan on 4/26/2017.
 */
public class FactionUtil {

    public static Faction getFaction(UUID uuid) {
        return MemoryFPlayers.getInstance().getByPlayer(Bukkit.getPlayer(uuid)).getFaction();
    }

}
