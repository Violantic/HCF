package me.finestdev.components.handlers;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.finestdev.components.Components;

public class LootingBuffHandler implements Listener {
	
	
	public LootingBuffHandler(){
		Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e){
		
		if (e.getEntity().getKiller() instanceof Player){
			Player p = e.getEntity().getKiller();
			if (p.getItemInHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS)){
				int dropped = e.getDroppedExp();
				int bonus = Components.getInstance().getConfig().getInt("looting-buffer");
				e.setDroppedExp(dropped * bonus);
			}
		}
	}
}
