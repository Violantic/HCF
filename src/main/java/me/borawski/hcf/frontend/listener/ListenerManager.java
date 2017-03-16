package me.borawski.hcf.frontend.listener;

import me.borawski.hcf.Core;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Ethan on 3/8/2017.
 */
public class ListenerManager {

    private Core instance;
    private List<Listener> listenerList;

    public ListenerManager(Core instance) {
        this.instance = instance;
        listenerList = new ArrayList<Listener>();
    }

    public Core getInstance() {
        return instance;
    }

    public List<Listener> getListenerList() {
        return listenerList;
    }

    public void addListener(Listener l) {
        getListenerList().add(l);
    }

    public void registerAll() {
        getListenerList().stream().forEach(new Consumer<Listener>() {
            @Override
            public void accept(Listener listener) {
                getInstance().getServer().getPluginManager().registerEvents(listener, getInstance());
            }
        });
    }
}
