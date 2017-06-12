package me.borawski.koth;

import com.massivecraft.factions.Factions;
import me.borawski.hcf.Core;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ethan on 4/26/2017.
 */
public class Plugin {

    public static final String PREFIX = ChatColor.DARK_RED + "" + ChatColor.BOLD + "KOTH " + ChatColor.GRAY;
    public static final String SEASON = "SEASON_BETA";
    public static final int KOTH_INTERVAL = 1200;
    public static int KOTH_TASKID = 0;
    public static AtomicReference<Integer> second = new AtomicReference<>();
    public static boolean enabled = true;
    private Koth currentKoth;
    private static Core instance;
    private KothManager kothManager;
    private List<UUID> playersAttending;

    private static Plugin internal;

    public Plugin(Core instance) {
        this.instance = instance;
        internal = this;
        //setFactionSession(new FactionSession(Mongo.getCollection("seasons").find(new Document("season", SEASON)).first()));
    }

    public static Core getInstance() {
        return instance;
    }

    public static Plugin getInternal() {
        return internal;
    }

    public void onEnable() {
        Factions factions = Factions.getInstance();
        if (factions == null) {
            //Factions is not on this server/isn't a supported version
            System.out.println("FACTIONS NOT FOUND");
        } else {
            System.out.println("Total Factions: " + factions.getAllFactions().size());
        }

        currentKoth = null;

        kothManager = new KothManager(this);
        kothManager.registerKoth();
        playersAttending = new ArrayList<>();

        if (kothManager.getScheduledKoth().size() == 0) {
            System.out.println(PREFIX + "No KOTH's registered... KOTH is disfunctional");
            return;
        }

        getInstance().getServer().getPluginManager().registerEvents(new KothListener(this), getInstance());

        second.set(1200);
        getInstance().getServer().getScheduler().runTaskTimer(getInstance(), new Runnable() {
            @Override
            public void run() {
                if (!enabled) return;
                if (second.get() == 1200) {
                    try {
                        getInstance().getServer().getScheduler().cancelTask(KOTH_TASKID);
                    } catch (Exception e) {
                        // Ignored. //
                    }

                    if(currentKoth == null) {
                        Random random = new Random();
                        int index = random.nextInt(getKothManager().getScheduledKoth().size());
                        currentKoth = getKothManager().getScheduledKoth().get(index);
                    }
                    second.set(0);
                    kothManager.updateScoreboard();
                    BukkitTask task = getInstance().getServer().getScheduler().runTaskTimerAsynchronously(getInstance(), currentKoth.getHandler(), 0L, 20L);
                    KOTH_TASKID = task.getTaskId();
                    return;
                }
                int newInt = second.get();
                second.set(++newInt);
            }
        }, 0L, 20L);
    }

    public Koth getCurrentKoth() {
        return currentKoth;
    }

    public void setCurrentKoth(Koth koth) {
        this.currentKoth = koth;
    }

    public KothManager getKothManager() {
        return kothManager;
    }

    public List<UUID> getPlayersAttending() {
        return playersAttending;
    }

}
