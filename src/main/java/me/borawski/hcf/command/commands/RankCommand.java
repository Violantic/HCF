package me.borawski.hcf.command.commands;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.commands.rank.RankCheckCommand;
import me.borawski.hcf.command.commands.rank.RankSetCommand;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 3/12/2017.
 */
public class RankCommand extends CustomBaseCommand {

    public RankCommand() {
        super("rank", "View your rank or manage others.", Rank.GUEST);
        addSubCommand(new RankCheckCommand());
        addSubCommand(new RankSetCommand());
    }

}
