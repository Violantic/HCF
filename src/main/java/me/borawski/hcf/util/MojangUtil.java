package me.borawski.hcf.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Ethan on 3/8/2017.
 * Credit to my friend Julian for helping me write this utility
 */
public class MojangUtil {

    private static final String BASE_URL = "https://mcapi.ca/";
    private static final JSONParser PARSER = new JSONParser();

    private MojangUtil() {}

    public static UUID getUniqueId(String name) throws IOException, ParseException {
        HttpURLConnection connection = ((HttpURLConnection) new URL(BASE_URL + "uuid/player/" + name).openConnection());
        JSONObject result = ((JSONObject) ((JSONArray) PARSER.parse(new InputStreamReader(connection.getInputStream()))).get(0));

        if(result.get("uuid_formatted") == null) {
            return null;
        }

        return UUID.fromString(((String) result.get("uuid_formatted")));
    }

    public static String getName(UUID uniqueId) throws IOException, ParseException {
        HttpURLConnection connection = ((HttpURLConnection) new URL(BASE_URL + "name/uuid/" + uniqueId.toString()).openConnection());
        JSONObject result = ((JSONObject) PARSER.parse(new InputStreamReader(connection.getInputStream())));

        return ((String) result.get("name"));
    }

}
