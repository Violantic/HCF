package com.massivecraft.factions;

import com.massivecraft.factions.zcore.persist.json.JSONBoard;

import java.util.ArrayList;
import java.util.Set;


public abstract class Board {
    protected static com.massivecraft.factions.Board instance = getBoardImpl();

    //----------------------------------------------//
    // Get and Set
    //----------------------------------------------//
    public abstract String getIdAt(com.massivecraft.factions.FLocation flocation);

    private static com.massivecraft.factions.Board getBoardImpl() {
        switch (Conf.backEnd) {
            case JSON:
                return new JSONBoard();
        }
        return null;
    }

    public static com.massivecraft.factions.Board getInstance() {
        return instance;
    }

    public abstract Faction getFactionAt(com.massivecraft.factions.FLocation flocation);

    public abstract void setIdAt(String id, com.massivecraft.factions.FLocation flocation);

    public abstract void setFactionAt(Faction faction, com.massivecraft.factions.FLocation flocation);

    public abstract void removeAt(com.massivecraft.factions.FLocation flocation);

    public abstract Set<com.massivecraft.factions.FLocation> getAllClaims(String factionId);

    public abstract Set<com.massivecraft.factions.FLocation> getAllClaims(Faction faction);

    // not to be confused with claims, ownership referring to further member-specific ownership of a claim
    public abstract void clearOwnershipAt(com.massivecraft.factions.FLocation flocation);

    public abstract void unclaimAll(String factionId);

    // Is this coord NOT completely surrounded by coords claimed by the same faction?
    // Simpler: Is there any nearby coord with a faction other than the faction here?
    public abstract boolean isBorderLocation(com.massivecraft.factions.FLocation flocation);

    // Is this coord connected to any coord claimed by the specified faction?
    public abstract boolean isConnectedLocation(com.massivecraft.factions.FLocation flocation, Faction faction);

    public abstract boolean hasFactionWithin(com.massivecraft.factions.FLocation flocation, Faction faction, int radius);

    //----------------------------------------------//
    // Cleaner. Remove orphaned foreign keys
    //----------------------------------------------//

    public abstract void clean();

    //----------------------------------------------//
    // Coord count
    //----------------------------------------------//

    public abstract int getFactionCoordCount(String factionId);

    public abstract int getFactionCoordCount(Faction faction);

    public abstract int getFactionCoordCountInWorld(Faction faction, String worldName);

    //----------------------------------------------//
    // Map generation
    //----------------------------------------------//

    /**
     * The map is relative to a coord and a faction north is in the direction of decreasing x east is in the direction
     * of decreasing z
     */
    public abstract ArrayList<String> getMap(Faction faction, FLocation flocation, double inDegrees);

    public abstract void forceSave();

    public abstract void forceSave(boolean sync);

    public abstract boolean load();
}
