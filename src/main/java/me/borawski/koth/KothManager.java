package me.borawski.koth;

import com.massivecraft.factions.listeners.FactionsPlayerListener;
import me.borawski.hcf.Core;
import me.borawski.hcf.session.FSession;
import me.borawski.hcf.util.BarUtil;
import me.borawski.koth.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Created by Ethan on 4/26/2017.
 */
public class KothManager {

    private Set<Koth> kothList;
    private List<Koth> scheduledKoth;
    private Map<String, Integer> score;
    private Scoreboard scoreboard;
    private String holdingFaction = null;
    private Plugin instance;
    public static boolean paused = false;
    public static AtomicReference<Integer> holdingScore = new AtomicReference<>();

    public KothManager(Plugin instance) {
        this.instance = instance;
        this.kothList = new HashSet<>();
        this.scheduledKoth = new ArrayList<>();
        this.score = new ConcurrentHashMap<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        setupScoreboard();
    }

    public void setupScoreboard() {
        final Objective objective = scoreboard.registerNewObjective("GameSB", "dummy");
        final org.bukkit.scoreboard.Team team = scoreboard.registerNewTeam("team1");
        final org.bukkit.scoreboard.Team team2 = scoreboard.registerNewTeam("team2");
        final org.bukkit.scoreboard.Team team3 = scoreboard.registerNewTeam("team3");
        final org.bukkit.scoreboard.Team team4 = scoreboard.registerNewTeam("team4");
        final org.bukkit.scoreboard.Team team5 = scoreboard.registerNewTeam("team5");
        final org.bukkit.scoreboard.Team team6 = scoreboard.registerNewTeam("team6");
        final org.bukkit.scoreboard.Team team7 = scoreboard.registerNewTeam("team7");
        final org.bukkit.scoreboard.Team team8 = scoreboard.registerNewTeam("team8");
        final org.bukkit.scoreboard.Team team9 = scoreboard.registerNewTeam("team9");
        final org.bukkit.scoreboard.Team team10 = scoreboard.registerNewTeam("team10");
        final org.bukkit.scoreboard.Team team11 = scoreboard.registerNewTeam("team11");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.AQUA.toString()));
        team2.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLACK.toString()));
        team3.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        team4.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA.toString()));
        team5.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_BLUE.toString()));
        team6.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY.toString()));
        team7.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN.toString()));
        team8.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_PURPLE.toString()));
        team9.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_RED.toString()));
        team10.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GOLD.toString()));

        objective.getScore(ChatColor.GOLD.toString()).setScore(1);
        objective.getScore(ChatColor.DARK_RED.toString()).setScore(2);
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(3);
        objective.getScore(ChatColor.DARK_GREEN.toString()).setScore(4);
        objective.getScore(ChatColor.DARK_GRAY.toString()).setScore(5);
        objective.getScore(ChatColor.DARK_BLUE.toString()).setScore(6);
        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(7);
        objective.getScore(ChatColor.BLUE.toString()).setScore(8);
        objective.getScore(ChatColor.BLACK.toString()).setScore(9);
        objective.getScore(ChatColor.AQUA.toString()).setScore(10);

        objective.setDisplayName(Core.getInstance().getPrefix());

        // Header
        // 0.  ""
        // 1.  "Rank:"
        // 2.  "► [RANK]"
        // 3.  "Faction"
        // 4.  "► FactionName"
        // 5.  "Koth"
        // 6.  "► Id:"
        // 7.  "► Captured:"
        // 8.  "► Factions:"
        // 9.  "► Players:"
        // 10. "► Timer: "

        team.setPrefix("§e§lSeason");
        team2.setPrefix("§7► ");
        team2.setSuffix("§c[BETA]");
        team3.setPrefix("§e§lFactions");
        team4.setPrefix("§7►");
        team5.setPrefix("§e§lKoth");
        team6.setPrefix("§7►Id:");
        team7.setPrefix("§7►Captured:");
        team8.setPrefix("§7►Factions:");
        team9.setPrefix("§7►Players:");
        team10.setPrefix("§7►Time:");
    }

    public void updateScoreboard() {
        List<String> factions = new ArrayList<>();
        Plugin.getInternal().getPlayersAttending().stream().forEach(new Consumer<UUID>() {
            @Override
            public void accept(UUID uuid) {
                if (!factions.contains(FactionsPlayerListener.factions.get(uuid))) {
                    factions.add(FactionsPlayerListener.factions.get(uuid));
                }
            }
        });

        List<String> factionsO = new ArrayList<>();
        Bukkit.getOnlinePlayers().stream().forEach((Consumer<Player>) uuid -> {
            if (!factionsO.contains(FactionsPlayerListener.factions.get(uuid.getUniqueId()))) {
                factionsO.add(FactionsPlayerListener.factions.get(uuid.getUniqueId()));
            }
        });

        getScoreboard().getTeam("team6").setSuffix(" §c" + Plugin.getInternal().getCurrentKoth().getName());
        getScoreboard().getTeam("team4").setSuffix(" §c" + factionsO.size());
        getScoreboard().getTeam("team7").setSuffix(" §c" + (holdingFaction == null ? ChatColor.RED + "\u2715" : ChatColor.GREEN + "\u2714"));
        getScoreboard().getTeam("team8").setSuffix(" §c" + factions.size());
        getScoreboard().getTeam("team9").setSuffix(" §c" + Plugin.getInternal().getPlayersAttending().size());

        if (holdingFaction != null && !paused) {
            int numberOfMinutes, numberOfSeconds;
            numberOfMinutes = (((holdingScore.get()) % 86400) % 3600) / 60;
            numberOfSeconds = (((holdingScore.get()) % 86400) % 3600) % 60;
            String seconds = ((numberOfSeconds < 10) ? ("0" + numberOfSeconds) : numberOfSeconds + "");
            final String minutes = ((numberOfMinutes < 10) ? ("0" + numberOfMinutes) + ":" : numberOfMinutes + ":") + seconds;
            getScoreboard().getTeam("team10").setPrefix(ChatColor.GRAY + "►Timer: ");
            getScoreboard().getTeam("team10").setSuffix(ChatColor.YELLOW + minutes);
        } else if (paused) {
            getScoreboard().getTeam("team10").setPrefix(ChatColor.GRAY + "►Paused");
            getScoreboard().getTeam("team10").setSuffix("");
        } else {
            getScoreboard().getTeam("team10").setPrefix(ChatColor.GRAY + "►Waiting");
            getScoreboard().getTeam("team10").setSuffix("");
        }
    }

    public Set<Koth> getKothSet() {
        return kothList;
    }

    public List<Koth> getScheduledKoth() {
        return scheduledKoth;
    }

    public Plugin getPlugin() {
        return instance;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public String getHoldingFaction() {
        return holdingFaction;
    }

    public void setHoldingFaction(String holdingFaction) {
        this.holdingFaction = holdingFaction;
    }

    /**
     * TODO: Iterate through config for Koth sessions
     */
    public void registerKoth() {
        for (String s : Plugin.getInstance().getConfig().getConfigurationSection("koth").getKeys(false)) {
            String name = Plugin.getInstance().getConfig().getString("koth." + s + ".name");
            String center = Plugin.getInstance().getConfig().getString("koth." + s + ".center_point");
            int radius = Plugin.getInstance().getConfig().getInt("koth." + s + ".radius");
            int length = Plugin.getInstance().getConfig().getInt("koth." + s + ".length");
            long currentTime = System.currentTimeMillis();
            long finish = currentTime + (length * 1000);
            getScheduledKoth().add(new Koth() {
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public Location getCenter() {
                    return LocationUtil.getLocation(center);
                }

                @Override
                public int radius() {
                    return radius;
                }

                @Override
                public List<String> getLog() {
                    return new ArrayList<String>();
                }

                @Override
                public Runnable getHandler() {
                    final int[] i = {0};
                    final int[] currentScore = {0};
                    final String[] lastFaction = {"N/A"};
                    return new Runnable() {
                        @Override
                        public void run() {
                            if (paused) return;
                            if (i[0] == 0) {
                                onStart();
                            } else {
                                if (currentScore[0] >= (60 * 15)) {
                                    onFinish();
                                    currentScore[0] = 0;
                                    i[0] = 0;
                                    return;
                                }

                                try {

                                    if(Plugin.getInternal().getPlayersAttending().size() == 0) {
                                        return;
                                    }
                                    if(FactionsPlayerListener.factions.get(Plugin.getInternal().getPlayersAttending().get(0)) == null) {
                                        return;
                                    }
                                    String name = FactionsPlayerListener.factions.get(Plugin.getInternal().getPlayersAttending().get(0));
                                    if (lastFaction[0].equalsIgnoreCase("N/A")) {
                                        currentScore[0] = 0;
                                        holdingScore.set(currentScore[0]);
                                        lastFaction[0] = name;
                                        holdingFaction = name;
                                        System.out.println(name + " has conquered the KOTH");
                                        getLog().add(name + " has conquered the KOTH");
                                    } else if (lastFaction[0].equalsIgnoreCase(name)) {
                                        currentScore[0]++;
                                        holdingScore.set(currentScore[0]);
                                        holdingFaction = name;
                                        //System.out.println(name + " is currently holding the KOTH: " + currentScore[0]);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                updateScoreboard();

                                try {
                                    Set<Map.Entry<UUID, String>> factionMembers = FactionsPlayerListener.factions.entrySet();
                                    factionMembers.stream().forEach(new Consumer<Map.Entry<UUID, String>>() {
                                        @Override
                                        public void accept(Map.Entry<UUID, String> uuidStringEntry) {
                                            if (uuidStringEntry.getValue().equalsIgnoreCase(name)) {
                                                Player player = Bukkit.getPlayer(uuidStringEntry.getKey());
                                                BarUtil.sendActionBar(player.getPlayer(), Plugin.PREFIX + "Your faction is holding " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + getName());
                                            }
                                        }
                                    });
                                } catch (Exception e) {

                                }
                            }
                            i[0]++;
                        }
                    };
                }

                @Override
                public int length() {
                    return length;
                }

                @Override
                public void onStart() {
                    Plugin.getInstance().getServer().broadcastMessage(Plugin.PREFIX + ChatColor.YELLOW + "" + ChatColor.BOLD + getName() + "" + ChatColor.RESET + "" + ChatColor.GRAY + " has started! " + ChatColor.YELLOW + "(/koth)");
                }

                @Override
                public void onFinish() {
                    Plugin.getInstance().getServer().getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Plugin.second.set(0);
                            Plugin.getInternal().setCurrentKoth(null);
                            new KothModel(null, getName(), holdingFaction, currentTime, finish, length(), Plugin.getInternal().getPlayersAttending(), getLog());
                            //getPlugin().getFactionSession().addScore(holdingFaction, "points", 350);
                            FSession.getSession(holdingFaction).updateDocument("factions", "trophies", FSession.getSession(holdingFaction).getTrophies() + 350);
                            holdingFaction = null;
                        }
                    });
                }
            });
        }
    }
}
