package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.hcf.frontend.gui.custom.settings.PlayerSettingsGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 3/21/2017.
 */
public class SettingsCommand implements Command {

    private Core instance;

    public SettingsCommand(Core instance) {
        this.instance = instance;
    }

    public Core getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public Rank requiredRank() {
        return Rank.VIP;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 1) {
            sender.sendMessage(getInstance().getPrefix() + "To change your settings, simply do " + ChatColor.YELLOW + "/settings");
            return;
        }
        new PlayerSettingsGUI(getInstance(), (Player) sender).show();
    }
}
