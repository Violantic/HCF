package me.finestdev.components.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.finestdev.components.Components;

public class MobStackHandler implements Listener {

	private List<EntityType> mobList;
	
	public MobStackHandler(){
		Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
		mobList = new ArrayList<EntityType>();
		loadEntityList();
		startStackTask();
	}
	
	public void loadEntityList() {
		if (!this.mobList.isEmpty()) {
			this.mobList.clear();
		}
		Iterator<String> iterator = Components.getInstance().getConfig().getStringList("stacking-entity").iterator();
		while (iterator.hasNext()) {
			this.mobList.add(EntityType.valueOf(iterator.next().toUpperCase()));
		}
	}

	public void startStackTask() {
		new BukkitRunnable() {
			public void run() {
				int mobStackingRadius = Components.getInstance().getConfig().getInt("stacking-radius");
				List<EntityType> mobList = MobStackHandler.this.mobList;
				Iterator<World> iterator = Bukkit.getServer().getWorlds().iterator();
				while (iterator.hasNext()) {
					for (LivingEntity livingEntity : iterator.next().getLivingEntities()) {
						if (mobList.contains(livingEntity.getType()) && livingEntity.isValid()) {
							for (Entity entity : livingEntity.getNearbyEntities((double) mobStackingRadius,
									(double) mobStackingRadius, (double) mobStackingRadius)) {
								if (entity instanceof LivingEntity && entity.isValid()
										&& mobList.contains(entity.getType())) {
									MobStackHandler.this.stackOne(livingEntity, (LivingEntity) entity, ChatColor
											.valueOf(Components.getInstance().getConfig().getString("stack-color")));
								}
							}
						}
					}
				}
			}
		}.runTaskTimer(Components.getInstance(), 40L, 40L);
	}

	public void unstackOne(LivingEntity livingEntity, ChatColor chatColor) {
		int amount = this.getAmount(livingEntity.getCustomName(), chatColor);
		if (amount <= 1) {
			return;
		}
		--amount;
		String string = chatColor + "x" + amount;
		LivingEntity livingEntity2 = (LivingEntity) livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), livingEntity.getType());
		livingEntity2.setCustomName(string);
		livingEntity2.setCustomNameVisible(true);
		livingEntity.setHealth(0.0);
	}

	public void stackOne(LivingEntity livingEntity, LivingEntity livingEntity2, ChatColor chatColor) {
		
		if (livingEntity.getType() != livingEntity2.getType()) {
			return;
		}
		int amount = this.getAmount(livingEntity.getCustomName(), chatColor);
		int amount2 = 1;
		if (this.isStacked(livingEntity2, chatColor)) {
			amount2 = this.getAmount(livingEntity2.getCustomName(), chatColor);
		}
		livingEntity2.remove();
		if (amount == 0) {
			livingEntity.setCustomName(chatColor + "x" + (amount2 + 1));
			livingEntity.setCustomNameVisible(true);
		} else {
			livingEntity.setCustomName(chatColor + "x" + (amount + amount2));
		}
	}

	public int getAmount(String s, ChatColor chatColor) {
		if (s == null) {
			return 0;
		}
		if (ChatColor.getLastColors(s).equals('§' + chatColor.getChar())) {
			return 0;
		}
		String stripColor = ChatColor.stripColor(ChatColor.stripColor(s.replace("x", "").replace("§f", "")));
		if (!stripColor.matches("[0-9]+")) {
			return 0;
		}
		if (stripColor.length() > 4) {
			return 0;
		}
		return Integer.parseInt(stripColor);
	}

	public boolean isStacked(LivingEntity livingEntity, ChatColor chatColor) {
		return this.getAmount(livingEntity.getCustomName(), chatColor) != 0;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent entityDeathEvent) {
		if (entityDeathEvent.getEntity() instanceof LivingEntity) {
			LivingEntity entity = entityDeathEvent.getEntity();
			if (entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.VILLAGER) {
				this.unstackOne(entity, ChatColor.valueOf(Components.getInstance().getConfig().getString("stack-color")));
			}
		}
	}
}