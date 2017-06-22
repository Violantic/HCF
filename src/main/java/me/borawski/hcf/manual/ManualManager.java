package me.borawski.hcf.manual;

import java.util.HashMap;
import java.util.Map;

import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 5/16/2017.
 */
public class ManualManager {

    private Map<Rank, Manual> manualMap;

    public ManualManager() {
        this.manualMap = new HashMap<>();
    }

    public Map<Rank, Manual> getManualMap() {
        return manualMap;
    }

    public void registerManual(Rank rank, Manual manual) {
        getManualMap().put(rank, manual);
    }

}
