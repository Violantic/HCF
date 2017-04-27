package me.finestdev.components.commands;

import me.borawski.hcf.backend.api.PlayerAPI;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.finestdev.components.Components;
import me.finestdev.components.utils.Utils;

public class EnderchestCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] array) {
		if (command.getName().equalsIgnoreCase("enderchest")) {
			if (commandSender instanceof Player) {
				Player player = (Player) commandSender;
				Session s = PlayerAPI.getSession(player);
				if (s.getRank().getId() >= Rank.MODERATOR.getId()) {
					if(Utils.enderchestDisabled == true){
						Utils.enderchestDisabled = false;
						Components.getInstance().getConfig().set("enderchest-disabled", false);
						Components.getInstance().saveConfig();
						player.sendMessage("Enderchest Enabled!");
					}else{
						player.sendMessage("Enderchest Disabled!");
						Components.getInstance().getConfig().set("enderchest-disabled", true);
						Components.getInstance().saveConfig();
						Utils.enderchestDisabled = true;
					}
				} else {
					player.sendMessage(Utils.noPerms());
				}
			} else {
				commandSender.sendMessage(Utils.noPerms());
			}
		}
		return true;
	}
}