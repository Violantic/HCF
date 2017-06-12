package me.borawski.hcf.gui.custom.friends;

import me.borawski.hcf.Core;
import me.borawski.hcf.gui.CustomIS;
import me.borawski.hcf.gui.ItemGUI;
import me.borawski.hcf.gui.MenuItem;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Ethan on 3/14/2017.
 */
public class FriendListGUI extends ItemGUI {

    public FriendListGUI(Core instance, Player p) {
        super(instance, null, p, 54);
    }

    @Override
    public String getName() {
        return "Friend List";
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        Session session = Session.getSession(getPlayer());
        int i = 0;
        for(UUID uuid : session.getFriends()) {
            String name = ChatUtils.getNameWithRankColor(uuid, true);
            String status = getInstance().getServer().getPlayer(uuid)==null?ChatColor.RED + "[OFFLINE]": ChatColor.GREEN + "[ONLINE]";
            String help = ChatColor.GRAY + "(Click to manage Friend)";
            set(i, new MenuItem(new CustomIS().setMaterial(Material.SKULL)
                    .setName(name)
                    .addLore(status), new Runnable() {
                @Override
                public void run() {

                }
            }));
            i++;
        }
    }
}
