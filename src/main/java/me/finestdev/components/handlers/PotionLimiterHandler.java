package me.finestdev.components.handlers;

import java.util.ArrayList;

import me.finestdev.components.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.finestdev.components.Components;

public class PotionLimiterHandler implements Listener {
	
	private ArrayList<PotionLimit> potionLimits;

	public PotionLimiterHandler() {
		this.potionLimits = new ArrayList<PotionLimit>();
		this.loadPotionLimits();
		Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
	}

	public void loadPotionLimits() {
		ConfigurationSection configurationSection = Components.getInstance().getConfig()
				.getConfigurationSection("potion-limiter");
		for (String s : configurationSection.getKeys(false)) {
			if (configurationSection.getInt(String.valueOf(s) + ".level") == -1) {
				continue;
			}
			PotionLimit potionLimit = new PotionLimit();
			potionLimit.setType(PotionEffectType.getByName(s));
			potionLimit.setLevel(configurationSection.getInt(String.valueOf(s) + ".level"));
			potionLimit.setExtended(configurationSection.getBoolean(String.valueOf(s) + ".extended"));
			this.potionLimits.add(potionLimit);
		}
	}

	public ArrayList<PotionLimit> getPotionLimits() {
		return this.potionLimits;
	}

	@EventHandler
	public void onPotionBrew(BrewEvent brewEvent) {
		BrewerInventory contents = brewEvent.getContents();
		ItemStack clone = contents.getIngredient().clone();
		ItemStack[] array = new ItemStack[3];
		for (int i = 0; i < 3; ++i) {
			if (brewEvent.getContents().getItem(i) != null) {
				array[i] = contents.getItem(i).clone();
			}
		}
		new BukkitRunnable() {
			public void run() {
				for (int i = 0; i < 3; ++i) {
					if (contents.getItem(i) != null) {
						for (PotionEffect potionEffect : Potion.fromItemStack(contents.getItem(i)).getEffects()) {
							for (PotionLimit potionLimit : PotionLimiterHandler.this.potionLimits) {
								if (potionLimit.getType().equals(potionEffect.getType())) {
									int level = potionLimit.getLevel();
									int n = potionEffect.getAmplifier() + 1;
									Potion fromItemStack = Potion.fromItemStack(contents.getItem(i));
									if (level == 0 || n > level) {
										contents.setIngredient(clone);
										for (int j = 0; j < 3; ++j) {
											contents.setItem(j, array[j]);
										}
										return;
									}
									if (fromItemStack.hasExtendedDuration() && !potionLimit.isExtended()) {
										contents.setIngredient(clone);
										for (int k = 0; k < 3; ++k) {
											contents.setItem(k, array[k]);
										}
										return;
									}
									continue;
								}
							}
						}
					}
				}
			}
		}.runTaskLater(Components.getInstance(), 1L);
	}

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent playerItemConsumeEvent) {
		Player player = playerItemConsumeEvent.getPlayer();
		ItemStack item = playerItemConsumeEvent.getItem();
		if (!item.getType().equals(Material.POTION)) {
			return;
		}
		if (item.getType().equals(Material.POTION) && item.getDurability() == 0) {
			return;
		}
		for (PotionEffect potionEffect : Potion.fromItemStack(item).getEffects()) {
			for (PotionLimit potionLimit : this.potionLimits) {
				if (potionLimit.getType().equals(potionEffect.getType())) {
					int level = potionLimit.getLevel();
					int n = potionEffect.getAmplifier() + 1;
					Potion fromItemStack = Potion.fromItemStack(item);
					if (level == 0 || n > level) {
						playerItemConsumeEvent.setCancelled(true);
						player.setItemInHand(new ItemStack(Material.AIR));
						player.sendMessage(Utils.chat("&4&lPOTIONS&r&7 This potion effect is disabled!"));
						return;
					}
					if (fromItemStack.hasExtendedDuration() && !potionLimit.isExtended()) {
						playerItemConsumeEvent.setCancelled(true);
						player.setItemInHand(new ItemStack(Material.AIR));
						player.sendMessage(Utils.chat("&4&lPOTIONS&r&7 This potion effect is disabled!"));
						return;
					}
					continue;
				}
			}
		}
	}

	@EventHandler
	public void onPotionSplash(PotionSplashEvent potionSplashEvent) {
		ThrownPotion potion = potionSplashEvent.getPotion();
		for (PotionEffect potionEffect : potion.getEffects()) {
			for (PotionLimit potionLimit : this.potionLimits) {
				if (potionLimit.getType().equals(potionEffect.getType())) {
					if (potion.getShooter() instanceof Player) {
						Player player = (Player) potion.getShooter();
						int level = potionLimit.getLevel();
						int n = potionEffect.getAmplifier() + 1;
						Potion fromItemStack = Potion.fromItemStack(potion.getItem());
						if (level == 0 || n > level) {
							potionSplashEvent.setCancelled(true);
							player.sendMessage(Utils.chat("&4&lPOTIONS&r&7 This potion effect is disabled!"));
							return;
						}
						if (fromItemStack.hasExtendedDuration() && !potionLimit.isExtended()) {
							potionSplashEvent.setCancelled(true);
							player.sendMessage("&4&lPOTIONS&r&7 This potion effect is disabled!");
							return;
						}
						continue;
					} else {
						int level2 = potionLimit.getLevel();
						int amplifier = potionEffect.getAmplifier();
						Potion fromItemStack2 = Potion.fromItemStack(potion.getItem());
						if (level2 == 0 || amplifier > level2) {
							potionSplashEvent.setCancelled(true);
							return;
						}
						if (fromItemStack2.hasExtendedDuration() && !potionLimit.isExtended()) {
							potionSplashEvent.setCancelled(true);
							return;
						}
						continue;
					}
				}
			}
		}
	}

	public class PotionLimit {
		private PotionEffectType type;
		private int level;
		private boolean extended;

		public PotionEffectType getType() {
			return this.type;
		}

		public void setType(PotionEffectType type) {
			this.type = type;
		}

		public int getLevel() {
			return this.level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public boolean isExtended() {
			return this.extended;
		}

		public void setExtended(boolean extended) {
			this.extended = extended;
		}
	}
}
