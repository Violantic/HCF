package me.borawski.hcf.gui.custom.edit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.borawski.hcf.gui.CustomIS;
import me.borawski.hcf.gui.ItemGUI;
import me.borawski.hcf.gui.MenuItem;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

/**
 * Created by Ethan on 3/8/2017.
 */
public class RankChangeGUI extends ItemGUI {

    private Session target;

    public RankChangeGUI(Player p, Session target) {
        super(null, p, 18);
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
        for (final Rank rank : Rank.values()) {
            set(i, new MenuItem(new CustomIS().setMaterial(Material.FIREWORK_CHARGE).setName(ChatColor.GRAY + rank.getDisplayName()).addLore(ChatColor.GRAY + "> Prefix: " + rank.getPrefix()).addLore(ChatColor.GRAY + "> Chat Color: " + rank.getColor() + rank.getColor().name()).addLore(ChatColor.GRAY + "> Id: " + ChatColor.YELLOW + rank.getId()),
                    new Runnable() {
                        @Override
                        public void run() {
                            target.setRank(rank);
                            SessionHandler.getInstance().save(target);
                        }
                    }));
            i++;
        }
    }
}
