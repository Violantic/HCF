package me.borawski.hcf.backend.util;

import me.borawski.hcf.backend.session.AchievementManager;
import me.borawski.hcf.backend.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 3/12/2017.
 */
public class FriendUtils {
    
    /*
     * Methods
     */

    public static void addFriend(Session player, UUID target) {
        player.getFriends().add(target);
        if(!player.hasAchievement("first_friend")) {
            player.awardAchievement(AchievementManager.FIRST_FRIEND, true);
        }
    }

    public static void removeFriend(Session player, UUID target) {
        player.getFriends().remove(target);
        saveFriends(player);
    }

    public static void addFriendRequest(Session player, UUID target, boolean incoming) {
        if (incoming) {
            player.getIncomingRequests().add(target);
        } else {
            player.getOutgoingRequests().add(target);
        }
        saveFriends(player);
    }

    public static void acceptFriendRequest(Session player, UUID target, boolean incoming) {
        addFriend(player, target);
        denyFriendRequest(player, target, incoming);
        saveFriends(player);
    }

    public static void denyFriendRequest(Session player, UUID target, boolean incoming) {
        if (incoming) {
            player.getIncomingRequests().remove(target);
        } else {
            player.getOutgoingRequests().remove(target);
        }
    }

    public static boolean hasRequest(Session player, UUID target) {
        return (player.getIncomingRequests().contains(target));
    }

    public static boolean isFriends(Session player, UUID target) {
        return (player.getFriends().contains(target));
    }

    private static void saveFriends(Session player) {
        player.updateDocument("players", "friend_requests", new ArrayList<List<UUID>>() {
            {
                add(player.getOutgoingRequests());
                add(player.getIncomingRequests());
            }
        });
        player.updateDocument("players", "friends", player.getFriends());
        player.dump();
        Session.getSession(player.getUUID());
    }

}
