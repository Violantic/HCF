package me.borawski.hcf.frontend.gui.custom.season;

import com.massivecraft.factions.listeners.FactionsPlayerListener;
import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.FactionSession;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.backend.util.FactionUtil;
import me.borawski.hcf.backend.util.SettingsUtil;
import me.borawski.hcf.frontend.gui.CustomIS;
import me.borawski.hcf.frontend.gui.ItemGUI;
import me.borawski.hcf.frontend.gui.MenuItem;
import me.borawski.koth.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ethan on 4/26/2017.
 */
public class FactionsLeadGUI extends ItemGUI {

    private static volatile int[][] gui = {
            {5, 5, 5, 5, 1, 5, 5, 5, 5},
            {5, 5, 3, 3, 3, 3, 3, 5, 5},
            {5, 3, 3, 3, 7, 3, 3, 3, 5},
            {5, 3, 2, 3, 6, 3, 10, 3, 5},
            {5, 5, 3, 3, 3, 3, 3, 5, 5},
            {5, 5, 5, 5, 5, 5, 5, 5, 5}
    };

    public FactionsLeadGUI(Core instance, Player p) {
        super(instance, null, p, 54);
    }

    @Override
    public String getName() {
        return "S. " + Plugin.SEASON.replace("_", " ").split(" ")[1];
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        FactionSession session = Plugin.getInternal().getFactionSession();
        int i = 0;
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 9; y++) {

                /**
                 * Settings labels
                 */

                try {
                    if (gui[x][y] == 1) {
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.DIAMOND).setName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + Plugin.SEASON.replace("_", " "))
                                .addLore(ChatColor.GRAY + "Below is the top 3 factions")
                                .addLore(ChatColor.GRAY + "for the current season of PVP"), new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    } else if (gui[x][y] == 7) {
                        String faction = FactionsPlayerListener.factions.get(getPlayer().getUniqueId());
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.PAPER).setName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Your Faction")
                                .addLore(ChatColor.GRAY + "----------------------")
                                .addLore(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + "" + ChatColor.BOLD + faction)
                                .addLore(ChatColor.GRAY + "Points: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getScore(FactionsPlayerListener.factions.get(getPlayer().getUniqueId())))
                                .addLore(ChatColor.GRAY + "KOTH Wins: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getKothWins(FactionsPlayerListener.factions.get(getPlayer().getUniqueId())))
                                .addLore(ChatColor.GRAY + "Top 3: " + ((Plugin.getInternal().getFactionSession().getFirstPlace().equalsIgnoreCase(faction)
                                        || Plugin.getInternal().getFactionSession().getSecondPlace().equalsIgnoreCase(faction)
                                        || Plugin.getInternal().getFactionSession().getThirdPlace().equalsIgnoreCase(faction)) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"))
                                .addLore(ChatColor.GRAY + "----------------------"), new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    } else if (gui[x][y] == 10) {
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.IRON_SWORD).setName(ChatColor.GRAY + "" + ChatColor.BOLD + "Third Place")
                                .addLore(ChatColor.GRAY + "----------------------")
                                .addLore((session.getThirdPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "Faction: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getThirdPlace())
                                .addLore((session.getThirdPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "Points: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getScore(session.getThirdPlace()))
                                .addLore((session.getThirdPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "KOTH Wins: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getKothWins(session.getThirdPlace()))
                                .addLore(ChatColor.GRAY + "----------------------")
                                , new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    } else if (gui[x][y] == 6) {
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.GOLD_SWORD).setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Second Place")
                                .addLore(ChatColor.GRAY + "----------------------")
                                .addLore((session.getSecondPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "Faction: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getSecondPlace())
                                .addLore((session.getSecondPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "Points: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getScore(session.getSecondPlace()))
                                .addLore((session.getSecondPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "KOTH Wins: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getKothWins(session.getSecondPlace()))
                                .addLore(ChatColor.GRAY + "----------------------"), new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    } else if (gui[x][y] == 2) {
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.DIAMOND_SWORD).setName(ChatColor.AQUA + "" + ChatColor.BOLD + "First Place")
                                .addLore(ChatColor.GRAY + "----------------------")
                                .addLore((session.getFirstPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "Faction: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getFirstPlace())
                                .addLore((session.getFirstPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "Points: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getScore(session.getFirstPlace()))
                                .addLore((session.getFirstPlace().equals(null))?ChatColor.GRAY + "N/A":ChatColor.GRAY + "KOTH Wins: " + ChatColor.YELLOW + "" + ChatColor.BOLD + session.getKothWins(session.getFirstPlace()))
                                .addLore(ChatColor.GRAY + "----------------------"), new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    } else if (gui[x][y] == 3) {
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setName("").setData(DyeColor.GRAY.getWoolData()), new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    } else if (gui[x][y] == 5) {
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setName("").setData(DyeColor.RED.getWoolData()), new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
