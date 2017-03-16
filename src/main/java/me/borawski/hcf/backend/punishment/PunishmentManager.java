package me.borawski.hcf.backend.punishment;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.connection.Mongo;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ethan on 3/12/2017.
 */
public class PunishmentManager {

    private Core instance;

    public PunishmentManager(Core instance) {
        this.instance = instance;
    }

    public void issue(Punishment.Type type, UUID player, UUID issuer, long until, String reason) {
        long time = until;
        TimeUnit timeUnit = TimeUnit.DAYS;
        long finalTime = System.currentTimeMillis() + (timeUnit.toMillis(time));
        Document document = new Document("uuid", player.toString());
        document.append("start", System.currentTimeMillis());
        document.append("end", finalTime);
        document.append("reason", reason.toString());
        document.append("issuer", issuer.toString());
        document.append("type", type.name());
        Mongo.getCollection("punishments").insertOne(document);
    }

}
