package me.borawski.hcf.backend.session;

import org.bukkit.ChatColor;

/**
 * Created by Ethan on 3/8/2017.
 */
public enum Rank {

    GUEST(1, "Guest", "&7", "", ChatColor.WHITE, ChatColor.GRAY),
    VIP(2, "VIP", "&b[VIP]&7", "", ChatColor.WHITE, ChatColor.AQUA),
    YOUTUBER(6, "YouTuber", "&e[YT]&7", "", ChatColor.WHITE, ChatColor.YELLOW),
    MODERATOR(7, "Moderator", "&9[MOD]&7", "", ChatColor.WHITE, ChatColor.BLUE),
    ADMIN(8, "Admin", "&c[ADMIN]&7", "", ChatColor.WHITE, ChatColor.RED),
    DEVELOPER(10, "Developer", "&c[DEV]&7", "", ChatColor.WHITE, ChatColor.RED),
    OWNER(11, "Owner", "&6[OWNER]&7", "", ChatColor.WHITE, ChatColor.GOLD);

    private final int id;
    private final String displayName;
    private final String prefix;
    private final String suffix;
    private final ChatColor color;
    private final ChatColor main;

    Rank(int id, String displayName, String prefix, String suffix, ChatColor color, ChatColor main) {
        this.id = id;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.main = main;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSuffix() {
        return suffix;
    }

    public ChatColor getColor() {
        return color;
    }

    public ChatColor getMain() {
        return main;
    }

    public String getPrefix() {
        return prefix.replace("&", ChatColor.COLOR_CHAR + "");
    }

}
