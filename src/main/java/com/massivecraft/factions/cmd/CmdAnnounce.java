package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdAnnounce extends FCommand {

    public CmdAnnounce() {
        super();
        this.aliases.add("ann");
        this.aliases.add("announce");

        this.requiredArgs.add("message");
        this.errorOnToManyArgs = false;

        this.permission = Permission.ANNOUNCE.node;
        this.disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = true;
    }

    @Override
    public void perform() {
        String prefix = TL.TITLE + "" + ChatColor.YELLOW + "[" + myFaction.getTag().toUpperCase() + "] " + ChatColor.GRAY + " ([" + FPlayers.getInstance().getByPlayer(me).getRole().getPrefix().toUpperCase() + "] " + me.getName() + "): " + ChatColor.GRAY;
        String message = StringUtils.join(args, " ");

        for (Player player : myFaction.getOnlinePlayers()) {
            player.sendMessage(prefix + message);
        }

        // Add for offline players.
        for (FPlayer fp : myFaction.getFPlayersWhereOnline(false)) {
            myFaction.addAnnouncement(fp, prefix + message);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_ANNOUNCE_DESCRIPTION;
    }

}
