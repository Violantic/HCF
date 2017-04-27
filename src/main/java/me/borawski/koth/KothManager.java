package me.borawski.koth;

import com.massivecraft.factions.FPlayers;
import me.borawski.hcf.backend.util.FactionUtil;
import me.borawski.koth.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by Ethan on 4/26/2017.
 */
public class KothManager {

    private Set<Koth> kothList;
    private List<Koth> scheduledKoth;
    private List<Koth> randomKoth;
    private boolean randomEnabled;
    private int random;
    private int scheduled;
    private Scoreboard scoreboard;
    private Plugin instance;

    public KothManager(Plugin instance) {
        this.instance = instance;
        this.kothList = new HashSet<>();
        this.scheduledKoth = new ArrayList<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
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

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.AQUA.toString()));
        team2.addPlayer(Bukkit.getOfflinePlayer(ChatColor.STRIKETHROUGH.toString()));
        team3.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLACK.toString()));
        team4.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        team5.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA.toString()));
        team6.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_PURPLE.toString()));
        team7.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN.toString()));
        team8.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_BLUE.toString()));
        team9.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY.toString()));
        objective.getScore(ChatColor.AQUA.toString()).setScore(9);
        objective.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(8);
        objective.getScore(ChatColor.BLACK.toString()).setScore(7);
        objective.getScore(ChatColor.BLUE.toString()).setScore(6);
        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(5);
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(4);
        objective.getScore(ChatColor.DARK_GREEN.toString()).setScore(3);
        objective.getScore(ChatColor.DARK_BLUE.toString()).setScore(2);
        objective.getScore(ChatColor.DARK_GRAY.toString()).setScore(1);

        objective.setDisplayName(Plugin.PREFIX);

        team.setPrefix("");
        team2.setPrefix(ChatColor.GRAY + "Holding");
        team3.setPrefix(ChatColor.YELLOW + "" + FactionUtil.getFaction(Plugin.getInternal().getCurrentKoth().getPlayers().get(0)).getTag().substring(0, 10));
        team4.setPrefix("");
        team5.setPrefix(ChatColor.GRAY + "Standing");
        team6.setPrefix(ChatColor.YELLOW + "1. " + FactionUtil.getFaction(Plugin.getInternal().getCurrentKoth().getPlayers().get(0)));
        team7.setPrefix(ChatColor.YELLOW + "2. " + getSecondPlace());
        team8.setPrefix(ChatColor.YELLOW + "3. " + getThirdPlace());
        team9.setPrefix("");
    }

    public Set<Koth> getKothSet() {
        return kothList;
    }

    public List<Koth> getScheduledKoth() {
        return scheduledKoth;
    }

    public List<Koth> getRandomKoth() {
        return randomKoth;
    }

    public boolean isRandomEnabled() {
        return randomEnabled;
    }

    public int getRandom() {
        return random;
    }

    public int getScheduled() {
        return scheduled;
    }

    public Plugin getPlugin() {
        return instance;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public String getFirstPlace() {
        return FactionUtil.getFaction(Plugin.getInternal().getCurrentKoth().getPlayers().get(0)).getTag();
    }

    public String getSecondPlace() {
        Map<String, Integer> copy = getPlugin().getCurrentKoth().getScore();
        try {
            copy.remove(getFirstPlace());
        } catch (Exception e) {
            System.out.println("Empty team(s)");
        }
        Map.Entry<String, Integer> secondPlace = null;
        for(Map.Entry<String, Integer> teams : getPlugin().getCurrentKoth().getScore().entrySet()) {
            if(secondPlace == null || teams.getValue() > secondPlace.getValue()) {
                secondPlace = teams;
            }
        }
        if (secondPlace != null) {
            return secondPlace.getKey();
        }
        return "N/A";
    }

    public String getThirdPlace() {
        Map<String, Integer> copy = getPlugin().getCurrentKoth().getScore();
        try {
            copy.remove(getFirstPlace());
            copy.remove(getSecondPlace());
        } catch (Exception e) {
            System.out.println("Empty team(s)");
        }
        Map.Entry<String, Integer> secondPlace = null;
        for(Map.Entry<String, Integer> teams : getPlugin().getCurrentKoth().getScore().entrySet()) {
            if(secondPlace == null || teams.getValue() > secondPlace.getValue()) {
                secondPlace = teams;
            }
        }
        if (secondPlace != null) {
            return secondPlace.getKey();
        }
        return "N/A";
    }

    public void updateScoreboard() {
        getScoreboard().getTeam("team3").setPrefix(ChatColor.YELLOW + "" + FactionUtil.getFaction(Plugin.getInternal().getCurrentKoth().getPlayers().get(0)).getTag().substring(0, 10));
        getScoreboard().getTeam("team6").setPrefix(ChatColor.YELLOW + "1. " + FactionUtil.getFaction(Plugin.getInternal().getCurrentKoth().getPlayers().get(0)));
    }

    /**
     * TODO: Iterate through config for Koth sessions
     */
    public void registerKoth() {
        Plugin.getInstance().getConfig().getConfigurationSection("koth").getKeys(false).stream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                String name = Plugin.getInstance().getConfig().getString(s + ".name");
                String point_one = Plugin.getInstance().getConfig().getString(s + ".first_point");
                String point_two = Plugin.getInstance().getConfig().getString(s + ".second_point");
                String center = Plugin.getInstance().getConfig().getString(s + ".center_point");
                int radius = Plugin.getInstance().getConfig().getInt(s + ".radius");
                int length = Plugin.getInstance().getConfig().getInt("s" + ".length");
                long currentTime = System.currentTimeMillis();
                long finish = currentTime + (length * 1000);
                getKothSet().add(new Koth() {
                    @Override
                    public String getName() {
                        return name;
                    }

                    @Override
                    public Location getFirstPoint() {
                        return LocationUtil.getLocation(point_one);
                    }

                    @Override
                    public Location getSecondPoint() {
                        return LocationUtil.getLocation(point_two);
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
                    public List<UUID> getPlayers() {
                        return new ArrayList<UUID>();
                    }

                    @Override
                    public Map<String, Integer> getScore() {
                        return new ConcurrentHashMap<String, Integer>();
                    }

                    @Override
                    public Runnable getHandler() {
                        final int[] i = {0};
                        return new Runnable() {
                            @Override
                            public void run() {
                                if (i[0] == 0) {
                                    onStart();
                                } else if (i[0] >= length()) {
                                    onFinish();
                                } else if (i[0] > Plugin.KOTH_INTERVAL) {
                                    onFinish();
                                } else if (getScore().get(getFirstPlace()) >= 360) {
                                    onFinish();
                                }
                                i[0]++;

                                String name = FactionUtil.getFaction(getPlayers().get(0)).getTag();
                                getScore().put(name, getScore().get(name) + 1);
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
                                Map.Entry<String, Integer> maxEntry = null;
                                for (Map.Entry<String, Integer> entry : getScore().entrySet()) {
                                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                                        maxEntry = entry;
                                    }
                                }

                                if (maxEntry == null) {
                                    return;
                                }

                                Plugin.KOTH_ID++;
                                Plugin.second.set(0);

                                new KothModel(null, getName(), maxEntry.getKey(), currentTime, finish, length(), getPlayers(), getLog());
                                getScore().entrySet().forEach(new Consumer<Map.Entry<String, Integer>>() {
                                    @Override
                                    public void accept(Map.Entry<String, Integer> stringIntegerEntry) {
                                        getPlugin().getFactionSession().addScore(stringIntegerEntry.getKey(), "points", stringIntegerEntry.getValue());
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
