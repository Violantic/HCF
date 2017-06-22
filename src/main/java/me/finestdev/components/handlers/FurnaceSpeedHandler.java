package me.finestdev.components.handlers;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.finestdev.components.Components;

public class FurnaceSpeedHandler implements Listener {

    public FurnaceSpeedHandler() {
        Bukkit.getPluginManager().registerEvents(this, Components.getInstance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            BlockState state = playerInteractEvent.getClickedBlock().getState();
            if (state instanceof Furnace) {
                Furnace furnace = (Furnace) state;
                furnace.setCookTime((short) (furnace.getCookTime() + Components.getInstance().getConfig().getInt("furnace.speed")));
                furnace.setBurnTime((short) Math.max(1, furnace.getBurnTime() - 1));
            }
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent furnaceBurnEvent) {
        BlockState state = furnaceBurnEvent.getBlock().getState();
        if (state instanceof Furnace) {
            Furnace furnace = (Furnace) state;
            if (Components.getInstance().getConfig().getInt("furnace.speed") > 1) {
                new FurnaceUpdateTask(furnace).runTaskTimer(Components.getInstance(), 1L, 1L);
            }
        }
    }

    public class FurnaceUpdateTask extends BukkitRunnable {
        private Furnace furnace;

        public FurnaceUpdateTask(Furnace furnace) {
            this.furnace = furnace;
        }

        public void run() {
            this.furnace.setCookTime((short) (this.furnace.getCookTime() + Components.getInstance().getConfig().getInt("furnace.speed")));
            this.furnace.setBurnTime((short) Math.max(1, this.furnace.getBurnTime() - 1));
            this.furnace.update();
            if (this.furnace.getBurnTime() <= 1) {
                this.cancel();
            }
        }
    }
}