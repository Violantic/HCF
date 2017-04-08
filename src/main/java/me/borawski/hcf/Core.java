package me.borawski.hcf;

import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.hcf.backend.punishment.PunishmentManager;
import me.borawski.hcf.backend.session.AchievementManager;
import me.borawski.hcf.frontend.command.CommandManager;
import me.borawski.hcf.frontend.command.commands.*;
import me.borawski.hcf.frontend.listener.ListenerManager;
import me.borawski.hcf.frontend.listener.PlayerListener;
import me.borawski.hcf.frontend.scoreboard.TabScoreboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Ethan on 3/8/2017.
 */
public class Core extends JavaPlugin {

    /**
     * Instance
     */
    private static Core instance;

    /**
     * Variables
     */
    private Mongo mongo;
    private ListenerManager listenerManager;
    private CommandManager commandManager;
    private PunishmentManager punishmentManager;
    private AchievementManager achievementManager;

    private TabScoreboard scoreboard;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(getConfig().contains("dbhost"));
        saveConfig();

        this.mongo = new Mongo();
        this.listenerManager = new ListenerManager(this);
        listenerManager.addListener(new PlayerListener(this));
        listenerManager.registerAll();
        this.commandManager = new CommandManager(this);
        registerCommands();
        this.punishmentManager = new PunishmentManager(this);
        this.achievementManager = new AchievementManager(this);

        this.scoreboard = new TabScoreboard(this);
    }

    /**
     * Instance
     */

    public static Core getInstance() {
        return instance;
    }

    public void registerCommands() {
        commandManager.add(new InfoCommand(this));
        commandManager.add(new BanCommand(this));
        commandManager.add(new RankCommand(this));
        commandManager.add(new FriendsCommand(this));
        commandManager.add(new AchievementCommand(this));
        commandManager.add(new SettingsCommand(this));
        commandManager.registerAll();
    }

    /**
     * Getters
     */

    public Mongo getMongo() {
        return mongo;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public TabScoreboard getScoreboard() {
        return scoreboard;
    }

    public String getPrefix() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF" + ChatColor.RESET + "" + ChatColor.GRAY + " ";
    }
}

