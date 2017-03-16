package me.borawski.hcf.backend.util;

import me.borawski.hcf.backend.connection.Mongo;
import org.bson.Document;

import java.util.UUID;

/**
 * Created by Ethan on 3/12/2017.
 */
public class PlayerUtils {

    public static boolean hasPlayed(String name) {
        Document document = Mongo.getCollection("players").find(new Document("name", name)).first();

        return document != null;
    }

    public static UUID getUUIDFromName(String name) {
        if (!hasPlayed(name)) return null;

        Document document = Mongo.getCollection("players").find(new Document("name", name)).first();
        return UUID.fromString(document.getString("uuid"));
    }

}
