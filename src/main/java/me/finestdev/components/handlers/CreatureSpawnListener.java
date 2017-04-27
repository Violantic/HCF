package me.finestdev.components.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import me.finestdev.components.Components;

public class CreatureSpawnListener implements Listener {

	public CreatureSpawnListener() {
		Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.NATURAL) {
			if (e.getEntity().getType() == EntityType.CREEPER || e.getEntity().getType() == EntityType.ENDERMAN)
				if (!Components.getInstance().getConfig().getBoolean("spawn-mobs") == false) {
					e.getEntity().remove();
				}
		}
	}
}