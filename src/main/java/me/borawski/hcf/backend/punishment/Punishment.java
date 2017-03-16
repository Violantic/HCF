package me.borawski.hcf.backend.punishment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import lombok.Getter;
import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.hcf.backend.util.UUIDUtil;
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

    @Getter
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
        this.issued = punishDoc.getLong("issued");
        this.until = punishDoc.getLong("until");
        this.undone = punishDoc.getBoolean("undone");
        this.reason = punishDoc.getString("reason");
    }

    /**
     * Universal Getters
     */

    public static Punishment getPunishment(UUID uuid, Type type) {
        return new Punishment(uuid, type, Mongo.getCollection("punishments").find(new Document("player", uuid)).first());
    }

    public static Punishment getCurrent(UUID uuid, Type type) {
        final Punishment[] p = new Punishment[1];
        getHistory(uuid, type).forEach(new Consumer<Punishment>() {
            @Override
            public void accept(Punishment punishment) {
                if(!punishment.undone && punishment.until == -1) {
                    p[0] = punishment;
                }
                if(!punishment.undone && System.currentTimeMillis() < punishment.until) {
                    p[0] = punishment;
                }
            }
        });

        return p[0];
    }

    public static List<Punishment> getHistory(UUID uuid, Type type) {
        List<Punishment> list = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("player", uuid);

        FindIterable<Document> cursor = Mongo.getCollection("punishments").find(query);
        cursor.forEach(new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                list.add(new Punishment(uuid, type, document));
            }
        });

        return list;
    }

    public enum Type {
        MUTE,
        BAN
    }

}
