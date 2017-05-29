package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.koth.Plugin;
import org.bukkit.command.CommandSender;

/**
 * Created by Ethan on 5/7/2017.
 */
public class FactionsVerboseCommand implements Command {

    @Override
    public String getName() {
        return "fverbose";
    }

    @Override
    public Rank requiredRank() {
        return Rank.ADMIN;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Plugin.getInternal().getFactionSession().document.toJson());
    }
}
