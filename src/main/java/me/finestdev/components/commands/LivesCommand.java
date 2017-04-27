package me.finestdev.components.commands;

import me.borawski.hcf.backend.api.PlayerAPI;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.finestdev.components.Components;
import me.finestdev.components.handlers.DeathBanHandler;
import me.finestdev.components.utils.Utils;

public class LivesCommand implements CommandExecutor {


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Session s = PlayerAPI.getSession((Player) sender);
		if (s.getRank().getId() >= Rank.MODERATOR.getId()) {

			if (args.length == 0) {
				sender.sendMessage(Utils.chat("&4&lLIVES&r &7/lives <player> <add:remove> <int>"));
				return true;
			} else if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target != null) {
					sender.sendMessage(Utils.chat(Utils.chat("&4&lLIVES&r &7The player " + target.getDisplayName()
							+ " &7currently has &c" + DeathBanHandler.getLives(target) + " lives")));
					return true;
				} else {
					sender.sendMessage(Utils.chat(Components.getInstance().getConfig().getString("player_not_found").replace("<target>", args[0])));
				}
			} else if (args.length == 2) {
				if (args[1].equalsIgnoreCase("add")) {
					sender.sendMessage(Utils.chat("&4&lLIVES&r &7/lives <player> add <int>"));
				} else if (args[1].equalsIgnoreCase("remove")) {
					sender.sendMessage(Utils.chat("&4&lLIVES&r &7/lives <player> remove <int>"));
				}
			} else if (args.length == 3) {
				int lives = Integer.parseInt(args[2]);
				Player target = Bukkit.getPlayer(args[0]);
				if (args[1].equalsIgnoreCase("add")) {
					if (!args[2].startsWith("-")) {
						DeathBanHandler.addLives(target.getPlayer(), lives);
						sender.sendMessage(Utils
								.chat("&4&lLIVES&r &7You have added &c" + lives + " lives &7to the player &6<target>")
								.replace("<target>", target.getDisplayName()));
						target.sendMessage(Utils.chat("&4&lLIVES&r &a<sender> &7has given you &c" + lives + " lives")
								.replace("<sender>", sender.getName()));
					} else {
						sender.sendMessage(Utils.chat("&4&lLIVES&r &cYou have entered an incorrect symbol"));
					}
				} else if (args[1].equalsIgnoreCase("remove")) {
					if (!args[2].startsWith("+")) {
						DeathBanHandler.takeLives(target.getPlayer(), lives);
						sender.sendMessage(Utils
								.chat("&4&lLIVES&r &7You have taken &c" + lives + " lives &7from the player &6<target>")
								.replace("<target>", target.getDisplayName()));
						target.sendMessage(
								Utils.chat("&4&lLIVES&r &a<sender> &7has taken &c" + lives + " lives &7from you")
										.replace("<sender>", sender.getName()));
					} else {
						sender.sendMessage(Utils.chat("&4&lLIVES&r &cYou have entered an incorrect symbol"));
					}
				}
			} else if (args.length > 3) {
				sender.sendMessage("Too many arguments");
				return true;
			}
		} else {
			sender.sendMessage(Utils.noPerms());
		}
		return false;
	}

}
