package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.gui.custom.season.FactionsLeadGUI;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 4/26/2017.
 */
public class SeasonCommand extends CustomCommand {

    public SeasonCommand() {
        super("Season", "View information on the current season.", Rank.GUEST);
    }


    @Override
    public void run(CommandSender sender, String label, String[] args) {
        new FactionsLeadGUI(Core.getInstance(), (Player) sender).show();
    }
}
