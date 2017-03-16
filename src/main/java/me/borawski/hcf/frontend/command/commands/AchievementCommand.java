package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Achievement;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.hcf.frontend.gui.CustomIS;
import me.borawski.hcf.frontend.gui.ItemGUI;
import me.borawski.hcf.frontend.gui.MenuItem;
import me.borawski.hcf.frontend.util.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 3/15/2017.
 */
public class AchievementCommand implements Command {

    private Core instance;
    public AchievementCommand(Core instance) {
        this.instance = instance;
    }

    public Core getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "achievements";
    }

    @Override
    public Rank requiredRank() {
        return Rank.GUEST;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            try {
                getAchievements(player.getUniqueId()).show();
            } catch (Exception e) {
                player.sendMessage(Core.getInstance().getPrefix() + "Error retrieving achievements!");
            }
        }
    }

    public ItemGUI getAchievements(UUID uuid) {
        return new ItemGUI(getInstance(), null, Bukkit.getPlayer(uuid), 54) {
            @Override
            public String getName() {
                return "Achivements";
            }

            @Override
            public boolean isCloseOnClick() {
                return false;
            }

            @Override
            public void registerItems() {
                List<String> achievements = Session.getSession(uuid).getAchievements();

                if(achievements.size() == 0) {
                    for(int i = 0; i < 54; i++) {
                        set(i, new MenuItem(new CustomIS()
                                .setMaterial(Material.STAINED_GLASS_PANE)
                                .setData(DyeColor.RED.getWoolData())
                                .setName(ChatColor.RED + "You haven't unlocked any achievements!")
                                .addLore(ChatColor.RED + "FeelsBadMan"), new Runnable() {
                            @Override
                            public void run() {

                            }
                        }));
                    }
                    return;
                }

                String title = ChatUtils.getNameWithRankColor(uuid, true);
                int i = 0;
                for(String s : achievements) {
                    Achievement achievement = getInstance().getAchievementManager().getAchievement(s);
                    set(i, new MenuItem(new CustomIS()
                            .setMaterial(Material.PAPER)
                            .setName(ChatColor.YELLOW + "Achievement #" + (++i))
                            .addLore(ChatColor.DARK_GRAY + "---------------------------")
                            .addLore(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + achievement.getName())
                            .addLore(ChatColor.GRAY + "Desc: " + ChatColor.YELLOW + achievement.getDesc())
                            .addLore(ChatColor.GRAY + "Reward: " + ChatColor.YELLOW + achievement.getReward() + " Tokens")
                            .addLore(ChatColor.DARK_GRAY + "---------------------------")
                            .addLore(ChatColor.GRAY + "This achievement was earned by: " + title), new Runnable() {
                        @Override
                        public void run() {
                            // Link in chat. //
                        }
                    }));
                    i++;
                }
            }
        };
    }
}
