package me.borawski.hcf.command;

import me.borawski.hcf.session.Rank;
import org.bukkit.command.CommandSender;

/**
 * Created by Ethan on 3/8/2017.
 */
public interface Command {

    String getName();
    Rank requiredRank();
    void execute(CommandSender sender, String[] args);

}
