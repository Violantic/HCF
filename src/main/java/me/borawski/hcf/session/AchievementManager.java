package me.borawski.hcf.session;

import me.borawski.hcf.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Ethan on 3/14/2017.
 */
public class AchievementManager {

    public static final Achievement FIRST_LOGIN = new Achievement("first_login", "First Login", "Login to DesireHCF for the first time", 100);
    public static final Achievement FIRST_FRIEND = new Achievement("first_friend", "First Friend", "Congratulations! You're making friends", 100);

    private Core instance;
    private List<Achievement> achievements;

    public AchievementManager(Core instance) {
        this.instance = instance;
        this.achievements = new ArrayList<Achievement>() {
            {
                add(FIRST_LOGIN);
                add(FIRST_FRIEND);
            }
        };
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public Achievement getAchievement(String id) {
        final Achievement[] a = new Achievement[1];
        getAchievements().stream().forEach(new Consumer<Achievement>() {
            @Override
            public void accept(Achievement achievement) {
                if (achievement.getId().equalsIgnoreCase(id)) {
                    a[0] = achievement;
                }
            }
        });

        return a[0];
    }

}
