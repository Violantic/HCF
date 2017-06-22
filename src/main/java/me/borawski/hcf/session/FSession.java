package me.borawski.hcf.session;

import me.borawski.hcf.Core;
import me.borawski.hcf.connection.Mongo;
import org.bson.Document;

import java.util.*;

/**
 * Created by Ethan on 5/19/2017.
 */
public class FSession {

    private static List<FSession> cache = new ArrayList<>();

    private Document factionDocument;
    private String name;
    private int trophies;
    private int tier;
    private int exp;
    private int koth;
    private List<UUID> members;
    private Map<String, String> settings;

    @SuppressWarnings("unchecked")
    public FSession(String name, Document playerDocument) {
        this.name = name;
        if (playerDocument == null && name != null) {
            playerDocument = new Document("name", name);
            playerDocument.append("name", name);
            playerDocument.append("trophies", 0);
            playerDocument.append("tier", 1);
            playerDocument.append("exp", 0);
            playerDocument.append("koth", 0);
            playerDocument.append("members", new ArrayList<UUID>());
            Mongo.getCollection("factions").insertOne(playerDocument);
            this.name = name;
            getSession(name);
        } else if (playerDocument != null) {
            this.factionDocument = playerDocument;
            this.name = playerDocument.getString("name");
            this.trophies = playerDocument.getInteger("trophies");
            this.tier = playerDocument.getInteger("tier");
            this.exp = playerDocument.getInteger("exp");
            this.koth = playerDocument.getInteger("koth");
            this.members = playerDocument.get("members", ArrayList.class);
            this.settings = playerDocument.get("settings", Map.class);
            // updateDocument("factions", "last_login",
            // System.currentTimeMillis());
            cache.add(this);
        }
    }

    /*
     * Getters
     */

    public static FSession getSession(String name) {
        for (int i = 0, cacheSize = cache.size(); i < cacheSize; i++) {
            FSession session = cache.get(i);
            if (session.getName().equalsIgnoreCase(name)) {
                return session;
            }
        }

        return new FSession(name, Mongo.getCollection("factions").find(new Document("name", name)).first());
    }

    public Document getFacDocument() {
        return this.factionDocument;
    }

    public String getName() {
        return name;
    }

    public int getTrophies() {
        return this.trophies;
    }

    public int getTier() {
        return this.tier;
    }

    public int getExp() {
        return this.exp;
    }

    public int getKoth() {
        return koth;
    }

    public List<UUID> getMembers() {
        return this.members;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    /*
     * Methods
     */

    public void dump() {
        cache.remove(this);
    }

    @SuppressWarnings("unchecked")
    public void updateDocument(String collection, String key, Object value) {
        Core.getInstance().getServer().getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Document document = getFacDocument().append(key, value);
                Mongo.getCollection(collection).findOneAndReplace(new Document("name", getName()), document);
            }
        });

        this.factionDocument = Mongo.getCollection(collection).find(new Document("name", getName())).first();
        this.trophies = getFacDocument().getInteger("trophies");
        this.tier = getFacDocument().getInteger("tier");
        this.exp = getFacDocument().getInteger("exp");
        this.koth = getFacDocument().getInteger("koth");
        this.members = getFacDocument().get("members", ArrayList.class);
        this.settings = getFacDocument().get("settings", Map.class);
    }

}
