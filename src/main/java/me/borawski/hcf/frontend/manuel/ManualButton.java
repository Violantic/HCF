package me.borawski.hcf.frontend.manuel;

import org.bukkit.ChatColor;

import java.util.List;

/**
 * Created by Ethan on 5/16/2017.
 */
public interface ManualButton {

    String getName();

    ChatColor getColor();

    List<String> getCommands();

}
