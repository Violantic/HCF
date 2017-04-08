package me.borawski.hcf.backend.util;

import me.borawski.hcf.backend.session.Session;

/**
 * Created by Ethan on 3/20/2017.
 */
public class SettingsUtil {

    /**
     * Only for use of true/false settings only!!!
     *
     * @param s
     * @param setting
     */
    public static void toggleSetting(Session s, String setting) {
        if (s.getSettings().get(setting).equals("true")) {
            s.getSettings().put(setting, "false");
        } else if (s.getSettings().get(setting).equals("false")) {
            s.getSettings().put(setting, "true");
        }
        s.updateDocument("players", "settings", s.getSettings());
    }

}
