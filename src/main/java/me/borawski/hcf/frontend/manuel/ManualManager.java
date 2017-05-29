package me.borawski.hcf.frontend.manuel;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 5/16/2017.
 */
public class ManualManager {

    private Core instance;
    private Map<Rank, Manual> manualMap;

    public ManualManager(Core instance) {
        this.instance = instance;
        this.manualMap = new HashMap<>();
    }

    public Core getInstance() {
        return instance;
    }

    public Map<Rank, Manual> getManualMap() {
        return manualMap;
    }

    public void registerManual(Rank rank, Manual manual) {
        getManualMap().put(rank, manual);
    }

}
