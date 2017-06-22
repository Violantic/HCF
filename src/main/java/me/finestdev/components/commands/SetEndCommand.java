package me.finestdev.components.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.finestdev.components.utils.Utils;

public class SetEndCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] array) {
        if (command.getName().equalsIgnoreCase("setendspawn")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (player.hasPermission("hcf.setendspawn")) {
                    Core.getInstance().getConfig().set("endspawn", Utils.toString(player.getLocation()));
                    Core.getInstance().saveConfig();
                    player.sendMessage(Utils.chat("&8[&c&l*&8]&7 You have set end spawn!"));
                } else {
                    player.sendMessage("No permission!");
                }
            } else {
                commandSender.sendMessage("No permission!");
            }
        }
        if (command.getName().equalsIgnoreCase("setendexit")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (player.hasPermission("hcf.setendexit")) {
                    Core.getInstance().getConfig().set("endexit", Utils.toString(player.getLocation()));
                    Core.getInstance().saveConfig();
                    player.sendMessage(Utils.chat("&8[&c&l*&8]&7 You have set end exit!"));
                } else {
                    player.sendMessage("No permission!");
                }
            } else {
                commandSender.sendMessage("No permission!");
            }
        }
        return true;
    }

}
