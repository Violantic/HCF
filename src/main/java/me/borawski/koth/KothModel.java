package me.borawski.koth;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.connection.Mongo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 4/26/2017.
 */
public class KothModel {

    private Document kothDocument;
    private String name;
    private String winner;
    private long start;
    private long finish;
    private int length;
    private List<UUID> players;
    private List<String> combatLog;

    public KothModel(Document kothDocument, String name, String winner, long start, long finish, int length, List<UUID> players, List<String> combatLog) {
        if(kothDocument == null && name != null && finish > 0 && finish > 0) {
            this.kothDocument = new Document("name", name);
            getKothDocument().append("name", name);
            getKothDocument().append("winner", winner);
            getKothDocument().append("start", System.currentTimeMillis());
            getKothDocument().append("finish", finish);
            getKothDocument().append("length", length);
            getKothDocument().append("players", players);
            getKothDocument().append("combat_log", combatLog);
            Mongo.getCollection("seasons").insertOne(getKothDocument());
        } else if(kothDocument != null) {
            this.kothDocument = kothDocument;
            this.name = name;
            this.winner = kothDocument.getString("winner");
            this.start = kothDocument.getLong("start");
            this.finish = kothDocument.getLong("finish");
            this.length = kothDocument.getInteger("length");
            this.players = kothDocument.get("players", ArrayList.class);
            this.combatLog = kothDocument.get("combat_log", ArrayList.class);
        }
    }

    public Document getKothDocument() {
        return kothDocument;
    }

    public String getName() {
        return name;
    }

    public String getWinner() {
        return winner;
    }

    public long getStart() {
        return start;
    }

    public long getFinish() {
        return finish;
    }

    public int getLength() {
        return length;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public List<String> getCombatLog() {
        return combatLog;
    }
}
