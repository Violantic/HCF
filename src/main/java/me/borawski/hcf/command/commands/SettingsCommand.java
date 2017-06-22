package me.borawski.hcf.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.gui.custom.settings.PlayerSettingsGUI;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 3/21/2017.
 */
public class SettingsCommand extends CustomCommand {

    public SettingsCommand() {
        super("settings", "Change your VIP settings.", Rank.VIP);
    }


    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(Core.getInstance().getPrefix() + "To change your settings, simply do " + ChatColor.YELLOW + "/settings");
            return;
        }
        new PlayerSettingsGUI(Core.getInstance(), (Player) sender).show();
    }
}
