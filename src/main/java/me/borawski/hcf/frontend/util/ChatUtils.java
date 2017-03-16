package me.borawski.hcf.frontend.util;

import me.borawski.hcf.backend.session.Session;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Ethan on 3/12/2017.
 */
public class ChatUtils {

    private final static int CENTER_PX = 154;

    /*
     * Methods
     */

    public static String prefix(String prefix) {
        return ChatColor.RED + "" + ChatColor.BOLD + prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " > " + ChatColor.RESET;
    }

    public static void sendCenteredMessage(Player player, String message) {
        if (message == null || message.equals("")) player.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == ChatColor.COLOR_CHAR) {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    /*
     * Getters
     */

    public static String getNameWithRankColor(Player player, boolean prefix) {
        if (prefix) {
            return Session.getSession(player).getRank().getMain() + Session.getSession(player).getRank().getPrefix() + " " + ChatColor.GRAY + player.getName() + ChatColor.RESET;
        } else {
            return Session.getSession(player).getRank().getMain() + player.getName() + ChatColor.RESET;
        }
    }

    public static String getNameWithRankColor(UUID uuid, boolean prefix) {
        if (prefix) {
            return Session.getSession(uuid).getRank().getMain() + Session.getSession(uuid).getRank().getPrefix() + " " + ChatColor.GRAY + Session.getSession(uuid).getName() + ChatColor.RESET;
        } else {
            return Session.getSession(uuid).getRank().getMain() + Session.getSession(uuid).getName() + ChatColor.RESET;
        }
    }


}
