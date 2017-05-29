package me.borawski.koth;

import org.bukkit.Location;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 4/26/2017.
 */
public interface Koth {

    String getName();

    Location getCenter();

    int radius();

    List<String> getLog();

    Runnable getHandler();

    int length();

    void onStart();

    void onFinish();
}
