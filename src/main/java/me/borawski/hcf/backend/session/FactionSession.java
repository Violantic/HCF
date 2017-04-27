package me.borawski.hcf.backend.session;

import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.koth.Plugin;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ethan on 4/26/2017.
 */
public class FactionSession {

    public Document document;

    public FactionSession(Document document) {
        if(document == null) {
            this.document = new Document("season", Plugin.SEASON);
        } else {
            this.document = document;
        }
    }

    public void registerFaction(String faction) {
        document.append(faction, new HashMap<String, Object>() {
            {
                put("points", 0);
                put("koth_wins", 0);
                put("event_wins", 0);
                put("kills", 0);
            }
        });
        Mongo.getCollection("seasons").insertOne(document);
    }

    public String getFirstPlace() {
        String name = "N/A";
        for(Map.Entry<String, Object> faction : document.entrySet()) {
            if(name.equalsIgnoreCase("") || (int) ((Map<String, Object>) faction.getValue()).get("points") > getScore(name)) {
                name = faction.getKey();
            }
        }
        return name;
    }

    public String getSecondPlace() {
        String name = "N/A";
        for(Map.Entry<String, Object> faction : document.entrySet()) {
            if(faction.getKey().equalsIgnoreCase(getFirstPlace())) {
                continue;
            }

            if(name.equalsIgnoreCase("N/A") || (int) ((Map<String, Object>) faction.getValue()).get("points") > getScore(name)) {
                name = faction.getKey();
            }
        }
        return name;
    }

    public String getThirdPlace() {
        Set<Map.Entry<String, Object>> copy = document.entrySet();
        String name = "N/A";
        for(Map.Entry<String, Object> faction : document.entrySet()) {
            if(faction.getKey().equalsIgnoreCase(getFirstPlace()) || faction.getKey().equalsIgnoreCase(getSecondPlace())) {
                continue;
            }

            if(name.equalsIgnoreCase("N/A") || (int) ((Map<String, Object>) faction.getValue()).get("points") > getScore(name)) {
                name = faction.getKey();
            }
        }
        return name;
    }

    public void addScore(String faction, String key, int value) {
        setScore(faction, key, getScore(faction)+value);
    }

    public void setScore(String faction, String key, int value) {
        Plugin.getInstance().getServer().getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                document.put(faction + "." + key, value);
            }
        });
    }

    public Integer getScore(String faction) {
        return document.getInteger(faction + ".points");
    }

    public Integer getKothWins(String faction) {
        return document.getInteger(faction + ".koth_wins");
    }

    public Integer getEventWins(String faction) {
        return document.getInteger(faction + ".event_wins");
    }
}
