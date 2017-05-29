package com.massivecraft.factions.iface;

import com.massivecraft.factions.struct.Relation;
import org.bukkit.ChatColor;

public interface RelationParticipator {

    public String describeTo(com.massivecraft.factions.iface.RelationParticipator that);

    public String describeTo(com.massivecraft.factions.iface.RelationParticipator that, boolean ucfirst);

    public Relation getRelationTo(com.massivecraft.factions.iface.RelationParticipator that);

    public Relation getRelationTo(com.massivecraft.factions.iface.RelationParticipator that, boolean ignorePeaceful);

    public ChatColor getColorTo(com.massivecraft.factions.iface.RelationParticipator to);
}
