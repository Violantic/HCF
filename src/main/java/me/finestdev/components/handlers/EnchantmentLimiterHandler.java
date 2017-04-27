package me.finestdev.components.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import me.finestdev.components.Components;

public class EnchantmentLimiterHandler implements Listener {

	private ArrayList<EnchantmentLimit> enchantmentLimits;

	public EnchantmentLimiterHandler() {
		this.enchantmentLimits = new ArrayList<EnchantmentLimit>();
		this.loadEnchantmentLimits();
		Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
	}

	public void loadEnchantmentLimits() {
		ConfigurationSection configurationSection = Components.getInstance().getConfig()
				.getConfigurationSection("enchantment-limiter");
		for (String s : configurationSection.getKeys(false)) {
			if (configurationSection.getInt(s) == -1) {
				continue;
			}
			EnchantmentLimit enchantmentLimit = new EnchantmentLimit();
			enchantmentLimit.setEnchantment(Enchantment.getByName(s));
			enchantmentLimit.setLevel(configurationSection.getInt(s));
			this.enchantmentLimits.add(enchantmentLimit);
		}
	}

	public ArrayList<EnchantmentLimit> getEnchantmentLimits() {
		return this.enchantmentLimits;
	}

	@EventHandler
	public void onEnchantItem(EnchantItemEvent enchantItemEvent) {
		Map<Enchantment, Integer> enchantsToAdd = enchantItemEvent.getEnchantsToAdd();
		for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
			if (enchantsToAdd.containsKey(enchantmentLimit.getEnchantment())
					&& enchantsToAdd.get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
				enchantsToAdd.remove(enchantmentLimit.getEnchantment());
				if (enchantmentLimit.getLevel() <= 0) {
					continue;
				}
				enchantsToAdd.put(enchantmentLimit.getEnchantment(), enchantmentLimit.getLevel());
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
		Player player = (Player) inventoryClickEvent.getWhoClicked();
		Inventory inventory = inventoryClickEvent.getInventory();
		InventoryType.SlotType slotType = inventoryClickEvent.getSlotType();
		if (inventory.getType().equals((Object) InventoryType.ANVIL)
				&& slotType.equals((Object) InventoryType.SlotType.RESULT)) {
			ItemStack currentItem = inventoryClickEvent.getCurrentItem();
			for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
				if (currentItem.getItemMeta().hasLore()) {
					Iterator<String> iterator2 = currentItem.getItemMeta().getLore().iterator();
					while (iterator2.hasNext()) {
						if (iterator2.next().equals(Components.getInstance().getConfig().getString("unrepairable-lore-line"))) {
							inventoryClickEvent.setCancelled(true);
							player.sendMessage("This item is not Repairable!");
						}
					}
				} else {
					if (currentItem.getType().equals((Object) Material.ENCHANTED_BOOK)) {
						EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) currentItem
								.getItemMeta();
						if (enchantmentStorageMeta.getStoredEnchants().containsKey(enchantmentLimit.getEnchantment())
								&&  enchantmentStorageMeta.getStoredEnchants()
										.get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
							inventoryClickEvent.setCancelled(true);
							player.sendMessage("You can't merge those Items!");
							return;
						}
					}
					if (currentItem.getEnchantments().containsKey(enchantmentLimit.getEnchantment())
							&&  currentItem.getEnchantments()
									.get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
						inventoryClickEvent.setCancelled(true);
						player.sendMessage("You can't merge those Items!");
						return;
					}
					continue;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerFish(PlayerFishEvent playerFishEvent) {
		Entity caught = playerFishEvent.getCaught();
		if (caught != null && caught instanceof ItemStack) {
			ItemStack itemStack = (ItemStack) caught;
			if (itemStack.getEnchantments() != null && !itemStack.getEnchantments().isEmpty()) {
				HashMap<Enchantment, Integer> hashMap = new HashMap<Enchantment, Integer>(itemStack.getEnchantments());
				for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
					if (hashMap.containsKey(enchantmentLimit.getEnchantment())
							&&  hashMap.get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
						hashMap.remove(enchantmentLimit.getEnchantment());
						if (enchantmentLimit.getLevel() <= 0) {
							continue;
						}
						hashMap.put(enchantmentLimit.getEnchantment(), enchantmentLimit.getLevel());
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent entityDeathEvent) {
		for (ItemStack itemStack : entityDeathEvent.getDrops()) {
			if (itemStack != null && !itemStack.getType().equals((Object) Material.AIR)
					&& itemStack.getEnchantments() != null && !itemStack.getEnchantments().isEmpty()) {
				HashMap<Enchantment, Integer> hashMap = new HashMap<Enchantment, Integer>(itemStack.getEnchantments());
				for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
					if (hashMap.containsKey(enchantmentLimit.getEnchantment())
							&&  hashMap.get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
						hashMap.remove(enchantmentLimit.getEnchantment());
						if (enchantmentLimit.getLevel() <= 0) {
							continue;
						}
						hashMap.put(enchantmentLimit.getEnchantment(), enchantmentLimit.getLevel());
					}
				}
			}
		}
	}

	public class EnchantmentLimit {
		private Enchantment enchantment;
		private int level;

		public Enchantment getEnchantment() {
			return this.enchantment;
		}

		public void setEnchantment(Enchantment enchantment) {
			this.enchantment = enchantment;
		}

		public int getLevel() {
			return this.level;
		}

		public void setLevel(int level) {
			this.level = level;
		}
	}
}