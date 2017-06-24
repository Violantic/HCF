package me.borawski.hcf.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

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
                } else
                    isBold = false;
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

    public static String getNameWithRankColor(Object o, boolean prefix) {
        Session session = SessionHandler.getSession(o);
        if (prefix) {
            return session.getRank().getMain() + session.getRank().getPrefix() + " " + ChatColor.GRAY + session.getName() + ChatColor.RESET;
        } else {
            return session.getRank().getMain() + session.getName() + ChatColor.RESET;
        }
    }

}
