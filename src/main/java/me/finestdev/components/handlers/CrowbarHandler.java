package me.finestdev.components.handlers;

import java.util.ArrayList;
import java.util.Iterator;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.borawski.hcf.backend.api.PlayerAPI;
import me.borawski.hcf.backend.session.Session;
import me.finestdev.components.MscAchievements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.finestdev.components.Components;
import me.finestdev.components.utils.Utils;

public class CrowbarHandler implements Listener {

	public CrowbarHandler() {
		Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getPlayer().getItemInHand();
		Action action = event.getAction();
		if (item == null) {
			return;
		}
		if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			Block clickedBlock = event.getClickedBlock();
			World world = clickedBlock.getWorld();
			Location location = clickedBlock.getLocation();
			if (this.isCrowbar(item)) {
				event.setCancelled(true);
				Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
				Faction faction2 = Board.getInstance().getFactionAt(new FLocation(clickedBlock.getLocation()));
				if (faction != faction2 && !faction2.isWilderness()) {
					return;
				}
				if (clickedBlock.getType().equals(Material.MOB_SPAWNER)) {
					int uses = this.getUses(item,
							Utils.chat(Components.getInstance().getConfig().getString("crowbar-spawners-string")
									+ Components.getInstance().getConfig().getString("crowbar-spawners-uses-string")));
					int uses2 = this.getUses(item,
							Utils.chat(Components.getInstance().getConfig().getString("crowbar-portals-string")
									+ Components.getInstance().getConfig().getString("crowbar-portals-uses-string")));
					if (uses > 0) {
						Session s = PlayerAPI.getSession(event.getPlayer());
						world.playEffect(location, Effect.STEP_SOUND, Material.MOB_SPAWNER.getId());
						ItemStack setItemName = setItemName(new ItemStack(Material.MOB_SPAWNER),
								String.valueOf(
										Components.getInstance().getConfig().getString("crowbar-spawner-name-color"))
										+ ((CreatureSpawner) clickedBlock.getState()).getSpawnedType().name()
										+ " Spawner");
						clickedBlock.setType(Material.AIR);
						world.dropItemNaturally(location, setItemName);
						if (--uses <= 0 && uses2 <= 0) {
							player.setItemInHand(new ItemStack(Material.AIR));
						} else {
							updateOrCreateCrowbarMeta(item, uses, uses2);
						}
						if(!s.hasAchievement("first_use_crowbar")) {
							s.awardAchievement(MscAchievements.FIRST_USE_CROWBAR, true);
						}
						player.updateInventory();
					} else {
						player.sendMessage("Crowbar has zero uses for spawners!");
					}
				} else if (clickedBlock.getType().equals(Material.ENDER_PORTAL_FRAME)) {
					int uses3 = this.getUses(item,
							Utils.chat(Components.getInstance().getConfig().getString("crowbar-spawners-string")
									+ Components.getInstance().getConfig().getString("crowbar-spawners-uses-string")));
					int uses4 = this.getUses(item,
							Utils.chat(Components.getInstance().getConfig().getString("crowbar-portals-string")
									+ Components.getInstance().getConfig().getString("crowbar-portals-uses-string")));
					if (uses4 > 0) {
						clickedBlock.setType(Material.AIR);
						world.playEffect(location, Effect.STEP_SOUND, Material.ENDER_PORTAL_FRAME.getId());
						world.dropItemNaturally(location, new ItemStack(Material.ENDER_PORTAL_FRAME));
						if (--uses4 <= 0 && uses3 <= 0) {
							player.setItemInHand(new ItemStack(Material.AIR));
						} else {
							updateOrCreateCrowbarMeta(item, uses3, uses4);
						}
						player.updateInventory();
					} else {
						player.sendMessage("Crowbar has zero uses for portals!");
					}
				} else {
					player.sendMessage("Spawners and Portal Frames only!");
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
		Block block = blockPlaceEvent.getBlock();
		ItemStack itemInHand = blockPlaceEvent.getItemInHand();
		String displayName = itemInHand.getItemMeta().getDisplayName();
		if (itemInHand.getType().equals(Material.MOB_SPAWNER) && itemInHand.getItemMeta().hasDisplayName()
				&& displayName.startsWith(Components.getInstance().getConfig().getString("crowbar-spawner-name-color"))
				&& displayName.endsWith(" Spawner")) {
			EntityType value = EntityType
					.valueOf(ChatColor.stripColor(displayName).replace(" Spawner", "").replace(" ", "_").toUpperCase());
			CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
			creatureSpawner.setSpawnedType(value);
			creatureSpawner.update();
		}
	}

	public boolean isCrowbar(ItemStack itemStack) {
		return itemStack != null && itemStack.getType() == Material.GOLD_HOE && itemStack.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(Components.getInstance().getConfig().getString("crowbar-name")));
	}

	public int getUses(ItemStack itemStack, String s) {
		if (itemStack != null && itemStack.getItemMeta().hasDisplayName()
				&& itemStack.getItemMeta().getDisplayName()
						.equals(Utils.chat(Components.getInstance().getConfig().getString("crowbar-name")))
				&& itemStack.getItemMeta().hasLore()) {
			int l = 0;
			for (String lore : itemStack.getItemMeta().getLore()) {
				if (lore.contains(s)) {
					String level = lore.replace(Utils.chat(s), "");
					l = Integer.parseInt(level);
				}
			}
			return l;
		}
		return 0;
	}

	public static ItemStack getNewCrowbar() {
		ItemStack itemStack = new ItemStack(Material.GOLD_HOE, 1);
		updateOrCreateCrowbarMeta(itemStack, Components.getInstance().getConfig().getInt("crowbar-uses-spawners"),
				Components.getInstance().getConfig().getInt("crowbar-uses-portals"));
		return itemStack;
	}

	public static ItemStack setItemName(final ItemStack itemStack, final String displayName) {
		final ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(Utils.chat(displayName));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static void updateOrCreateCrowbarMeta(ItemStack itemStack, int spawner, int portals) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				Components.getInstance().getConfig().getString("crowbar-name")));
		ArrayList<String> lore = new ArrayList<String>();
		Iterator<String> iterator = Components.getInstance().getConfig().getStringList("crowbar-lore").iterator();
		String spawnerCountLore = Components.getInstance().getConfig().getString("crowbar-spawners-string")
				+ Components.getInstance().getConfig().getString("crowbar-spawners-uses-string")
				+ Integer.toString(spawner);
		String portalsCountLore = Components.getInstance().getConfig().getString("crowbar-portals-string")
				+ Components.getInstance().getConfig().getString("crowbar-portals-uses-string")
				+ Integer.toString(portals);
		while (iterator.hasNext()) {
			lore.add(Utils.chat(iterator.next().replaceAll("<spawner-string>", spawnerCountLore)
					.replaceAll("<portal-string>", portalsCountLore)));
		}
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
	}
}
