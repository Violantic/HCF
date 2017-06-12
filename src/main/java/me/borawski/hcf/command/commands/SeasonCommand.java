package me.borawski.hcf.command.commands;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.Command;
import me.borawski.hcf.gui.custom.season.FactionsLeadGUI;
import me.borawski.hcf.session.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 4/26/2017.
 */
public class SeasonCommand implements Command {

    @Override
    public String getName() {
        return "season";
    }

    @Override
    public Rank requiredRank() {
        return Rank.GUEST;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        new FactionsLeadGUI(Core.getInstance(), (Player) sender).show();
    }
}
