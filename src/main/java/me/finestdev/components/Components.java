package me.finestdev.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Bukkit;

import me.borawski.hcf.Core;
import me.finestdev.components.commands.CrowbarCommand;
import me.finestdev.components.commands.EnderchestCommand;
import me.finestdev.components.commands.LivesCommand;
import me.finestdev.components.commands.SetEndCommand;
import me.finestdev.components.handlers.BrewingSpeedHandler;
import me.finestdev.components.handlers.CombatHandler;
import me.finestdev.components.handlers.CreatureSpawnListener;
import me.finestdev.components.handlers.CrowbarHandler;
import me.finestdev.components.handlers.DeathBanHandler;
import me.finestdev.components.handlers.EnchantmentLimiterHandler;
import me.finestdev.components.handlers.EnderchestHandler;
import me.finestdev.components.handlers.EnderpearlHandler;
import me.finestdev.components.handlers.FurnaceSpeedHandler;
import me.finestdev.components.handlers.GappleHandler;
import me.finestdev.components.handlers.LootingBuffHandler;
import me.finestdev.components.handlers.MobStackHandler;
import me.finestdev.components.handlers.PotionLimiterHandler;
import me.finestdev.components.handlers.PvPTimer;
import me.finestdev.components.handlers.RegionHandler;
import me.finestdev.components.handlers.SellSignHandler;
import me.finestdev.components.utils.Cooldown;

public class Components {

    private static Components instance;

    public final static String GAPPLE = "GAPPLE";
    public final static String CBTLOG = "CBTLOG";
    public final static String ENDERP = "ENDERP";
    public final static String PVPT = "PVPT";
    public final static String DEATHBAN = "DEATHBAN";

    private Map<String, Cooldown> cooldowns = new HashMap<>();

    private DeathBanHandler playerDeath;

    public void onEnable() {
        instance = this;
        cooldowns.clear();
        checkDependencies();
        registerCooldowns();
        registerListeners();
        registerCommands();

    }

    public void onDisable() {
        instance = null;
        saveLives();
    }

    public void registerListeners() {
        new CrowbarHandler();
        new MobStackHandler();
        new FurnaceSpeedHandler();
        new GappleHandler();
        new EnderpearlHandler();
        new CreatureSpawnListener();
        new RegionHandler();
        new CombatHandler();
        new EnderchestHandler();
        new BrewingSpeedHandler();
        new LootingBuffHandler();
        playerDeath = new DeathBanHandler();
        new SellSignHandler();
        new EnchantmentLimiterHandler();
        new PotionLimiterHandler();
        new PvPTimer();
    }

    public void registerCommands() {
        Core.getInstance().getCommand("crowbar").setExecutor(new CrowbarCommand());
        Core.getInstance().getCommand("enderchest").setExecutor(new EnderchestCommand());
        Core.getInstance().getCommand("lives").setExecutor(new LivesCommand());
        Core.getInstance().getCommand("setendspawn").setExecutor(new SetEndCommand());
        Core.getInstance().getCommand("setendexit").setExecutor(new SetEndCommand());
    }

    public static Components getInstance() {
        return instance;
    }

    public void registerCooldowns() {
        File cooldownFolder = new File(Core.getInstance().getDataFolder() + File.separator + "cooldowns");
        if (!cooldownFolder.exists())
            cooldownFolder.mkdirs();
        File gapFile = new File(cooldownFolder, "gapple.cooldown");
        cooldowns.put(GAPPLE, new Cooldown(gapFile));
        File cbtLogFile = new File(cooldownFolder, "cbtlog.cooldown");
        cooldowns.put(CBTLOG, new Cooldown(cbtLogFile));
        File pvpTFile = new File(cooldownFolder, "pvpt.cooldown");
        cooldowns.put(PVPT, new Cooldown(pvpTFile));
        File enderPFile = new File(cooldownFolder, "enderp.cooldown");
        cooldowns.put(ENDERP, new Cooldown(enderPFile));
        File dbanFile = new File(cooldownFolder, "dban.cooldown");
        cooldowns.put(DEATHBAN, new Cooldown(dbanFile));
        cooldowns.values().forEach(cooldown -> cooldown.startRunning());
    }

    public void checkDependencies() {
        try {
            Scanner scan = new Scanner(new File(Core.getInstance().getDataFolder(), "items.csv"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] values = line.split(",");
                if (!line.startsWith("#") && values.length == 3) {
                    SellSignHandler.namesToIds.put(values[0].toLowerCase(), values[1] + ":" + values[2]);
                }
            }
            Bukkit.getLogger().info("Successfully loaded items.csv!");
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveLives() {
        cooldowns.values().forEach(cooldown -> {
            try {
                cooldown.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        playerDeath.saveLives();
    }

    public Cooldown getCooldown(String s) {
        return cooldowns.get(s);
    }
}
