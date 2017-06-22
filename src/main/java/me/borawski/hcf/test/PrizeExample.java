package me.borawski.hcf.test;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by Ethan on 4/26/2017.
 */
public class PrizeExample extends JavaPlugin implements Listener {

    /**
     * Variables
     */
    public static final String PREFIX = ChatColor.RED + "" + ChatColor.BOLD + "PRIZES " + ChatColor.GRAY;
    private List<Prize> prizes;

    @Override
    public void onEnable() {

        /**
         * Instantiate Variables
         */
        prizes = new ArrayList<>();

        /**
         * Process container of Prizes
         */
        getConfig().getConfigurationSection("prizes").getKeys(false).stream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                final String name = getConfig().getString(s + ".name");
                final List<String> commands = getConfig().getStringList(s + ".commands");
                final Integer percent = getConfig().getInt(s + ".percent");
                getPrizes().add(new Prize() {
                    @Override
                    public String getName() {
                        return name;
                    }

                    @Override
                    public List<String> getCommands() {
                        return commands;
                    }

                    @Override
                    public int percent() {
                        return percent;
                    }
                });
                System.out.println(PREFIX + name + " has been loaded into memory");
            }
        });

        /**
         * Register Listeners
         */
        getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * Listener event for when the player logs into the server
     * 
     * @param event
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        final Prize[] prizes = new Prize[1];
        final boolean[] finished = { false };
        getPrizes().stream().forEach(new Consumer<Prize>() {
            @Override
            public void accept(Prize prize) {
                if (finished[0]) {
                    return;
                }
                int percent = new Random().nextInt(100) + 1;
                if (percent >= prize.percent()) {
                    prizes[0] = prize;
                    finished[0] = true;
                }
            }
        });
        try {
            prizes[0].getCommands().stream().forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    getServer().dispatchCommand(getServer().getConsoleSender(), s.replace("%player%", event.getPlayer().getName()).replace("%prefix%", PREFIX).replace("&", ChatColor.COLOR_CHAR + ""));
                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(PREFIX + "Exception when processing prize picking logic");
        }
    }

    /**
     * Getters
     * 
     * @return
     */
    public List<Prize> getPrizes() {
        return prizes;
    }

    /**
     * Prize interface to be instantiated upon load
     */
    public interface Prize {
        String getName();

        List<String> getCommands();

        int percent();
    }

}
