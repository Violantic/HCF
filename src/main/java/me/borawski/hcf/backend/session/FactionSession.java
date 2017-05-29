package me.borawski.hcf.backend.session;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.koth.Plugin;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by Ethan on 4/26/2017.
 */
public class FactionSession {

    public Document document;
    private Map<String, Map<String, Object>> factionSession;

    public FactionSession(Document document) {
        if (document == null) {
            this.document = new Document("season", Plugin.SEASON);
            this.document.append("last_update", System.currentTimeMillis());
            Mongo.getCollection("factions").insertOne(this.document);
        } else {
            this.document = document;
        }
        this.factionSession = new ConcurrentHashMap<>();
    }

    public Document getDocument() {
        return document;
    }

    public Map<String, Map<String, Object>> getFactionSession() {
        return factionSession;
    }

    public void registerFaction(String faction) {
        if (getFactionSession().containsKey(faction)) {
            return;
        }

        if (document.get(faction, Map.class) == null) {
            Map map = new HashMap<String, Object>() {
                {
                    put("points", 0);
                    put("koth_wins", 0);
                    put("event_wins", 0);
                    put("kills", 0);
                }
            };

            new BukkitRunnable() {
                public void run() {
                    document.append(faction, map);
                    document.append("last_update", System.currentTimeMillis());
                    System.out.println("Didn't have data for: " + faction + " - storing and caching");
                    //Mongo.getCollection("factions").findOneAndReplace(new Document("name", Plugin.SEASON), document);
                }
            }.runTaskAsynchronously(Core.getInstance());
        } else {
            System.out.println("Already had stored data for: " + faction + " - caching instead");
            Map<String, Object> map = (Map<String, Object>) document.get(faction, Map.class);
            getFactionSession().put(faction, map);
        }
    }

    public String getFirstPlace() {
        final String[] name = {null};
        Set<Map.Entry<String, Object>> faction = document.entrySet();
        faction.stream().forEach(new Consumer<Map.Entry<String, Object>>() {
            @Override
            public void accept(Map.Entry<String, Object> stringObjectEntry) {
                if (stringObjectEntry.getKey().startsWith("_") || stringObjectEntry.getKey().equalsIgnoreCase("season") || stringObjectEntry.getKey().equalsIgnoreCase("last_update")) {
                    return;
                }

                if (name[0] == null) {
                    name[0] = stringObjectEntry.getKey();
                } else {
                    Integer highest = getScore(name[0]);
                    try {
                        Map<String, Object> map = (Map<String, Object>) stringObjectEntry.getValue();
                        if ((Integer) map.get("score") >= highest) {
                            name[0] = stringObjectEntry.getKey();
                        }
                    } catch (Exception exception) {
                        //System.out.printf("HCF: Error getting map from document!");
                    }
                }
            }
        });
        return name[0];
    }

    public String getSecondPlace() {
        final String[] name = {null};
        Set<Map.Entry<String, Object>> faction = document.entrySet();
        faction.stream().forEach(new Consumer<Map.Entry<String, Object>>() {
            @Override
            public void accept(Map.Entry<String, Object> stringObjectEntry) {
                if (stringObjectEntry.getKey().startsWith("_") || stringObjectEntry.getKey().equalsIgnoreCase("season") || stringObjectEntry.getKey().equalsIgnoreCase("last_update")) {
                    return;
                }

                if (name[0] == null) {
                    name[0] = stringObjectEntry.getKey();
                } else if (!stringObjectEntry.getKey().equalsIgnoreCase(getFirstPlace())) {
                    Integer highest = getScore(name[0]);
                    try {
                        Map<String, Object> map = (Map<String, Object>) stringObjectEntry.getValue();
                        if ((Integer) map.get("score") >= highest) {
                            name[0] = stringObjectEntry.getKey();
                        }
                    } catch (Exception exception) {
                        //System.out.printf("HCF: Error getting map from document!");
                    }
                }
            }
        });
        return name[0];
    }

    public String getThirdPlace() {
        final String[] name = {null};
        Set<Map.Entry<String, Object>> faction = document.entrySet();
        faction.stream().forEach(new Consumer<Map.Entry<String, Object>>() {
            @Override
            public void accept(Map.Entry<String, Object> stringObjectEntry) {
                if (stringObjectEntry.getKey().startsWith("_") || stringObjectEntry.getKey().equalsIgnoreCase("season") || stringObjectEntry.getKey().equalsIgnoreCase("last_update")) {
                    return;
                }

                if (name[0] == null) {
                    name[0] = stringObjectEntry.getKey();
                } else if (!stringObjectEntry.getKey().equalsIgnoreCase(getFirstPlace()) || !stringObjectEntry.getKey().equalsIgnoreCase(getSecondPlace())) {
                    Integer highest = getScore(name[0]);
                    try {
                        Map<String, Object> map = (Map<String, Object>) stringObjectEntry.getValue();
                        if ((Integer) map.get("score") >= highest) {
                            name[0] = stringObjectEntry.getKey();
                        }
                    } catch (Exception exception) {
                        //System.out.printf("HCF: Error getting map from document!");
                    }
                }
            }
        });
        return name[0];
    }

    public void addScore(String faction, String key, int value) {
        setScore(faction, key, getScore(faction) + value);
    }

    public void setScore(String faction, String key, int value) {
        Map<String, Object> factionValues = new HashMap<String, Object>() {
            {
                put("points", getScore(faction));
                put("koth_wins", getKothWins(faction));
                put("event_wins", getEventWins(faction));
                // Unimplemented as of YET. //
                put("kills", 0);
            }
        };

        factionValues.put(key, value);
        Plugin.getInstance().getServer().getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                getDocument().append(faction, factionValues);
            }
        });
    }

    public Integer getScore(String faction) {
        Map<String, Object> map = (Map<String, Object>) document.get(faction, Map.class);
        return (Integer) map.get("points");
    }

    public Integer getKothWins(String faction) {
        Map<String, Object> map = (Map<String, Object>) document.get(faction, Map.class);
        return (Integer) map.get("koth_wins");
    }

    public Integer getEventWins(String faction) {
        Map<String, Object> map = (Map<String, Object>) document.get(faction, Map.class);
        return (Integer) map.get("event_wins");
    }
}
