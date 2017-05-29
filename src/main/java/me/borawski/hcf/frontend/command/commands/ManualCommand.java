package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.hcf.frontend.util.ManualUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 5/17/2017.
 */
public class ManualCommand implements Command {

    @Override
    public String getName() {
        return "manual";
    }

    @Override
    public Rank requiredRank() {
        return Rank.YOUTUBER;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("yt")) {
                System.out.println("Attempting to open manual");
                ManualUtil.openManual(Rank.YOUTUBER, (Player) sender);
            }
        }
    }
}
