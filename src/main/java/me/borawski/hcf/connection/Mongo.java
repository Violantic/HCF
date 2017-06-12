package me.borawski.hcf.connection;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.borawski.hcf.Core;
import org.bson.Document;

/**
 * Created by Ethan on 3/12/2017.
 */
public class Mongo {

    private static MongoDatabase mongoDatabase;
    private MongoClient mongoClient;

    public Mongo() {
        this.mongoClient = new MongoClient(new ServerAddress(Core.getInstance().getConfig().getString("dbhost"), Core.getInstance().getConfig().getInt("dbport")));
        mongoDatabase = this.mongoClient.getDatabase("core");
    }

    /*
     * Getters
     */

    public static MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public static MongoCollection<Document> getCollection(String collection) {
        return getMongoDatabase().getCollection(collection);
    }


}
