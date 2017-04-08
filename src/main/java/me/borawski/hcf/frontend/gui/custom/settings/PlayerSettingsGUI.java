package me.borawski.hcf.frontend.gui.custom.settings;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.backend.util.SettingsUtil;
import me.borawski.hcf.frontend.gui.CustomIS;
import me.borawski.hcf.frontend.gui.ItemGUI;
import me.borawski.hcf.frontend.gui.MenuItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ethan on 3/20/2017.
 */
public class PlayerSettingsGUI extends ItemGUI {

    private static volatile int[][] gui = {
            {3, 3, 3, 3, 1, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 2, 3, 6, 3, 3, 3},
            {3, 3, 3, 4, 3, 12, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3}
    };

    public PlayerSettingsGUI(Core instance, Player p) {
        super(instance, null, p, 54);
    }

    @Override
    public String getName() {
        return "Settings";
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        Session s = Session.getSession(getPlayer());
        int i = 0;
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 9; y++) {

                /**
                 * Settings labels
                 */

                if (gui[x][y] == 1) {
                    set(i, new MenuItem(new CustomIS().setMaterial(Material.COMPASS).setName(ChatColor.YELLOW + "User Settings").addLore(ChatColor.GRAY + "Below is your settings management"), new Runnable() {
                        @Override
                        public void run() {

                        }
                    }));
                } else if (gui[x][y] == 2) {
                    set(i, new MenuItem(new CustomIS().setMaterial(Material.SKULL_ITEM).setName(ChatColor.YELLOW + "Friend Requests").addLore(ChatColor.GRAY + "Allows users to send you requests"), new Runnable() {
                        @Override
                        public void run() {

                        }
                    }));
                } else if(gui[x][y] == 6) {
                    set(i, new MenuItem(new CustomIS().setMaterial(Material.BOOK).setName(ChatColor.YELLOW + "Private Messaging").addLore(ChatColor.GRAY + "Allows users to send you messages"), new Runnable() {
                        @Override
                        public void run() {

                        }
                    }));
                }

                /**
                 * Settings change buttons
                 */

                else if(gui[x][y] == 4) {
                    String label = (s.getSettings().get("friend_requests").equals("true"))?ChatColor.GREEN + "[ENABLED]":ChatColor.RED + "[DISABLED]";
                    ItemStack stack = new CustomIS().setMaterial(Material.WOOL).setData((s.getSettings().get("friend_requests").equals("true"))? DyeColor.LIME.getWoolData():DyeColor.RED.getWoolData()).get();
                    set(i, new MenuItem(new CustomIS(stack).setName(label).addLore(ChatColor.GRAY + "(Click to change)"), new Runnable() {
                        @Override
                        public void run() {
                            SettingsUtil.toggleSetting(s, "friend_requests");
                            new PlayerSettingsGUI(getInstance(), getPlayer()).show();
                        }
                    }));
                } else if(gui[x][y] == 12) {
                    String label = (s.getSettings().get("private_messaging").equals("true")?ChatColor.GREEN + "[ENABLED]":ChatColor.RED + "[DISABLED]");
                    ItemStack stack = new CustomIS().setMaterial(Material.WOOL).setData((s.getSettings().get("private_messaging").equals("true"))? DyeColor.LIME.getWoolData():DyeColor.RED.getWoolData()).get();
                    set(i, new MenuItem(new CustomIS(stack).setName(label).addLore(ChatColor.GRAY + "(Click to change)"), new Runnable() {
                        @Override
                        public void run() {
                            SettingsUtil.toggleSetting(s, "private_messaging");
                            new PlayerSettingsGUI(getInstance(), getPlayer()).show();
                        }
                    }));
                }
                i++;
            }
        }
    }
}
