package me.borawski.hcf.backend.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.zcore.persist.json.JSONFPlayer;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Ethan on 4/26/2017.
 */
public class FactionUtil {

    public static Faction getFaction(UUID uuid) {
        FPlayer player = FPlayers.getInstance().getByPlayer(Bukkit.getPlayer(uuid));
        return player.getFaction();
    }

}
