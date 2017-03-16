package me.borawski.hcf.frontend.gui.custom.edit;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.frontend.gui.CustomIS;
import me.borawski.hcf.frontend.gui.ItemGUI;
import me.borawski.hcf.frontend.gui.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 3/8/2017.
 */
public class RankChangeGUI extends ItemGUI {

    private Session target;

    public RankChangeGUI(Core instance, Player p, Session target) {
        super(instance, null, p, 18);
        this.target = target;
    }

    public Session getTarget() {
        return target;
    }

    @Override
    public String getName() {
        return "Ranks";
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        int i = 0;
        for(final Rank rank : Rank.values()) {
            set(i, new MenuItem(new CustomIS().setMaterial(Material.FIREWORK_CHARGE).setName(ChatColor.GRAY + rank.getDisplayName())
                    .addLore(ChatColor.GRAY + "> Prefix: " + rank.getPrefix())
                    .addLore(ChatColor.GRAY + "> Chat Color: " + rank.getColor() + rank.getColor().name())
                    .addLore(ChatColor.GRAY + "> Id: " + ChatColor.YELLOW + rank.getId()), new Runnable() {
                @Override
                public void run() {
                    RankChangeGUI.this.getTarget().updateDocument("players", "rank", rank.name());
                    getInstance().getScoreboard().apply(Bukkit.getPlayer(getTarget().getUUID()));
                }
            }));
            i++;
        }
    }
}
