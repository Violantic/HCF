package me.borawski.hcf.frontend.gui.custom;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.frontend.gui.CustomIS;
import me.borawski.hcf.frontend.gui.ItemGUI;
import me.borawski.hcf.frontend.gui.MenuItem;
import me.borawski.hcf.frontend.gui.custom.edit.PlayerPunishmentsGUI;
import me.borawski.hcf.frontend.gui.custom.edit.RankChangeGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 3/8/2017.
 */
public class PlayerInfoGUI extends ItemGUI {

    public static Map<UUID, Session> crossTarget = new HashMap<>();

    public PlayerInfoGUI(Core instance, Player player) {
        super(instance, null, player, 9);
    }

    @Override
    public String getName() {
        return "Player Info";
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        Session s1 = crossTarget.get(getPlayer().getUniqueId());
        set(0, new MenuItem(new CustomIS().setMaterial(Material.NAME_TAG).setName(ChatColor.GRAY + "UUID: " + s1.getUUID()), new Runnable() {
            @Override
            public void run() {

            }
        }));
        set(1, new MenuItem(new CustomIS().setMaterial(Material.NAME_TAG).setName(ChatColor.GRAY + "NAME: " + s1.getName()), new Runnable() {
            @Override
            public void run() {

            }
        }));
        set(2, new MenuItem(new CustomIS().setMaterial(Material.NAME_TAG).setName(ChatColor.GRAY + "IP: 10.0.0.1 (N/A)"), new Runnable() {
            @Override
            public void run() {

            }
        }));
        set(4, new MenuItem(new CustomIS().setMaterial(Material.FIREWORK_CHARGE).setName(ChatColor.GRAY + "RANK: " + s1.getRank().getPrefix()).addLore(ChatColor.GRAY + "(Click to edit rank)"), new Runnable() {
            @Override
            public void run() {
                new RankChangeGUI(getInstance(), getPlayer(), s1).show();
            }
        }));
        set(5, new MenuItem(new CustomIS().setMaterial(Material.ANVIL).setName(ChatColor.GRAY + "PUNISHMENTS").addLore(ChatColor.GRAY + "(Click to manage their punishments)").addLore(ChatColor.GRAY + "(Coming Soon)"), new Runnable() {
            @Override
            public void run() {
                new PlayerPunishmentsGUI(getInstance(), getPlayer(), s1);
            }
        }));
        crossTarget.remove(getPlayer().getUniqueId());
    }
}
