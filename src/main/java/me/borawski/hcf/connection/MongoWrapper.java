package me.borawski.hcf.connection;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.FileHandler;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.punishment.PunishmentHandler;
import me.borawski.hcf.session.FactionSession;
import me.borawski.hcf.session.FactionSessionHandler;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

/**
 * Created by Ethan on 3/12/2017.
 */
public class MongoWrapper {

    private MongoClient mc;
    private Morphia morphia;
    private Datastore datastore;

    public MongoWrapper() {
        FileHandler config = Core.getConfigHandler();
        ServerAddress addr = new ServerAddress(config.getString("database.host"), config.getInteger("database.port"));

        List<MongoCredential> credentials = new ArrayList<>();
        credentials.add(MongoCredential.createCredential(config.getString("database.username"), config.getString("database.database"), config.getString("database.password").toCharArray()));

        mc = new MongoClient(addr, credentials);
        morphia = new Morphia();

        morphia.map(Session.class);
        morphia.map(Punishment.class);
        morphia.map(FactionSession.class);

        SessionHandler.initialize();
        PunishmentHandler.initialize();
        FactionSessionHandler.initialize();

        datastore = morphia.createDatastore(mc, config.getString("database.database"));
        datastore.ensureIndexes();
    }

    public MongoClient getMongoClient() {
        return mc;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public Datastore getDatastore() {
        return datastore;
    }

}
