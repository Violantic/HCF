package me.borawski.hcf.frontend.util;

import io.netty.buffer.Unpooled;
import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.frontend.manuel.Manual;
import me.borawski.hcf.frontend.manuel.ManualPage;
import me.borawski.hcf.frontend.manuel.servermanuals.YouTuberManual;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan on 5/16/2017.
 */
public class ManualUtil {

    public static void initializeManuals(Map<Rank, Manual> map) {
        map.put(Rank.YOUTUBER, new YouTuberManual());
    }

    public static ItemStack newBook(Rank rank) {
        BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        meta.setTitle(rank.getMain() + rank.getDisplayName() + " Manual");
        meta.setAuthor("DesireHCF");

        List<String> stringList = new ArrayList<>();
        //stringList.add(getCover(rank).getContents().toString().replace("[", "").replace("]", "").replace(",", ""));
        for(ManualPage s : Core.getInstance().getManualManager().getManualMap().get(rank).getPages()) {
            String page = s.getContents().toString().replace("[", "").replace("]", "").replace(",", "");
            stringList.add(page);
        }

        meta.setPages(stringList);
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        return book;
    }

    public static void openManual(Rank rank, Player p) {
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, newBook(rank));
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.EMPTY_BUFFER));
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public static ManualPage getCover(Rank rank) {
        return new ManualPage("cover")
                .addString("&7Welcome to the")
                .addString("\n" + rank.getPrefix() + " Manual")
                .addString("")
                .addString("\n")
                .addString("\n" + "&4&lYOUR RANK:")
                .addString("\n")
                .addString("\n" + "&7Name: " + rank.getMain() + rank.getDisplayName().toUpperCase() + "")
                .addString("\n" + "&7Perm: &a" + rank.getId() + "")
                .addString("\n" + "&7Tag: " + rank.getPrefix() + "")
                .addString("\n");
    }

}
