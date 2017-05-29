package me.borawski.hcf.backend.session;

import org.bukkit.ChatColor;

/**
 * Created by Ethan on 3/8/2017.
 */
public enum Rank {

    GUEST(1, "Guest", "&7⧫&7", "", ChatColor.GRAY, ChatColor.GRAY),
    BRIGADIER(2, "Brigadier", "&a⧫&7", "", ChatColor.WHITE, ChatColor.GREEN),
    COMMODORE(2, "Commodore", "&b⧫&7", "", ChatColor.WHITE, ChatColor.AQUA),
    GRANDMASTER(2, "Grandmaster", "&d⧫&7", "", ChatColor.WHITE, ChatColor.LIGHT_PURPLE),
    VIP(3, "VIP", "&3[VIP]&7", "", ChatColor.WHITE, ChatColor.AQUA),
    YOUTUBER(6, "YouTuber", "&6[YT]&7", "", ChatColor.WHITE, ChatColor.GOLD),
    MODERATOR(7, "Moderator", "&9[MOD]&7", "", ChatColor.WHITE, ChatColor.BLUE),
    ADMIN(8, "Admin", "&c[ADMIN]&7", "", ChatColor.WHITE, ChatColor.RED),
    DEVELOPER(10, "Developer", "&c[DEV]&7", "", ChatColor.WHITE, ChatColor.RED),
    OWNER(11, "Owner", "&c[OWNER]&7", "", ChatColor.WHITE, ChatColor.RED);

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
