package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.hcf.frontend.gui.custom.season.FactionsLeadGUI;
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
