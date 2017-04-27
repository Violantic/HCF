package me.finestdev.components.handlers;

import me.finestdev.components.Components;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BrewingSpeedHandler implements Listener {
	
	public BrewingSpeedHandler(){
		Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
		if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
			BlockState state = playerInteractEvent.getClickedBlock().getState();
			if (state instanceof BrewingStand) {
				BrewingStand brewingStand = (BrewingStand) state;
				brewingStand.setBrewingTime((short) (brewingStand.getBrewingTime() + Components.getInstance().getConfig().getInt("brewing.speed")));
				brewingStand.setBrewingTime((short) Math.max(1, brewingStand.getBrewingTime() - 1));
			}
		}
	}

	@EventHandler
	public void onBrew(BrewEvent brewEvent) {
		BlockState state = brewEvent.getBlock().getState();
		if (state instanceof BrewingStand) {
			BrewingStand brewingStand = (BrewingStand) state;
			if (Components.getInstance().getConfig().getInt("brewing.speed") > 1) {
				new BrewingUpdateTask(brewingStand).runTaskTimer(Components.getInstance(), 1L, 1L);
			}
		}
	}

	public class BrewingUpdateTask extends BukkitRunnable {
		private BrewingStand brewingStand;

		public BrewingUpdateTask(BrewingStand brewingStand) {
			this.brewingStand = brewingStand;
		}

		public void run() {
			this.brewingStand.setBrewingTime((short) (this.brewingStand.getBrewingTime() + Components.getInstance().getConfig().getInt("brewing.speed")));
			this.brewingStand.setBrewingTime((short) Math.max(1, this.brewingStand.getBrewingTime() - 1));
			this.brewingStand.update();
			if (this.brewingStand.getBrewingTime() <= 1) {
				this.cancel();
			}
		}
	}
}