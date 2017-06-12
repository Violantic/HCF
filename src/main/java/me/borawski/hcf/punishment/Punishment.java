package me.borawski.hcf.punishment;

import me.borawski.hcf.connection.Mongo;
import org.bson.Document;
import org.jongo.marshall.jackson.oid.MongoId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Ethan on 3/8/2017.
 */
public class Punishment {

    private UUID player;
    private UUID issuer;
    private Type type;
    private long issued;
    private long until;
    private boolean undone;
    private String reason;

    public Punishment(UUID player, Type type, Document punishDoc) {
        this.player = player;
        this.type = type;
        this.issuer = UUID.fromString(punishDoc.getString("issuer"));
        //this.issued = punishDoc.getLong("issued");
        //this.until = punishDoc.getLong("until");
        //this.undone = punishDoc.getBoolean("undone");
        //this.reason = punishDoc.getString("reason");
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getIssuer() {
        return issuer;
    }

    public Type getType() {
        return type;
    }

    public long getIssued() {
        return issued;
    }

    public long getUntil() {
        return until;
    }

    public boolean isUndone() {
        return undone;
    }

    public String getReason() {
        return reason;
    }

    /**
     * Universal Getters
     */

    public static Punishment getPunishment(UUID uuid, Type type) {
        return new Punishment(uuid, type, Mongo.getCollection("punishments").find(new Document("uuid", uuid)).first());
    }

    public enum Type {
        MUTE,
        BAN
    }

}
