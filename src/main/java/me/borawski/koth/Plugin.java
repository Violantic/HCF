package me.borawski.koth;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.hcf.backend.session.FactionSession;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ethan on 4/26/2017.
 */
public class Plugin {

    public static final String PREFIX = ChatColor.DARK_RED + "" + ChatColor.BOLD + "KOTH " + ChatColor.GRAY;
    public static final String SEASON = "SEASON_BETA";
    public static final int KOTH_INTERVAL = getInstance().getConfig().getInt("koth_schedule_interval");
    public static int KOTH_ID = 0;
    public static int KOTH_TASKID = 0;
    public static AtomicReference<Integer> second = new AtomicReference<>();
    public static boolean enabled = getInstance().getConfig().getBoolean("koth_enabled");
    private static Core instance;
    private KothManager kothManager;
    private FactionSession factionSession;

    private static Plugin internal;

    public Plugin(Core instance) {
        this.instance = instance;
        internal = this;
    }

    public static Core getInstance() {
        return instance;
    }

    public static Plugin getInternal() {
        return internal;
    }

    public void onEnable() {
        try {
            kothManager = new KothManager(this);
            kothManager.registerKoth();

            if(kothManager.getScheduledKoth().size() == 0) {
                System.out.println(PREFIX + "No KOTH's registered... KOTH is disfunctional");
                return;
            }

            if(KOTH_ID > kothManager.getScheduledKoth().size()) {
                KOTH_ID = 0;
            }
            second.set(0);
            //setFactionSession(new FactionSession(Mongo.getCollection("seasons").find(new Document("season", SEASON)).first()));
            getInstance().getServer().getScheduler().runTaskTimer(getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!enabled) return;
                    if (second.get() == 0) {
                        try {
                            getInstance().getServer().getScheduler().cancelTask(KOTH_TASKID);
                        } catch (Exception e) {
                            // Ignored. //
                        }

                        setFactionSession(new FactionSession(Mongo.getCollection("seasons").find(new Document("season", SEASON)).first()));
                        BukkitTask task = getInstance().getServer().getScheduler().runTaskTimer(getInstance(), kothManager.getScheduledKoth().get(KOTH_ID).getHandler(), 0L, 20L);
                        KOTH_TASKID = task.getTaskId();
                    }
                    int newInt = second.get();
                    second.set(++newInt);
                }
            }, 0L, 20L);
        } catch (Exception e) {
            System.out.println(PREFIX + "Could not fetch faction leader board for KOTH!");
        }
    }

    public Koth getCurrentKoth() {
        return getKothManager().getScheduledKoth().get(KOTH_ID);
    }

    public KothManager getKothManager() {
        return kothManager;
    }

    public FactionSession getFactionSession() {
        return factionSession;
    }

    public void setFactionSession(FactionSession s) {
        this.factionSession = s;
    }
}
