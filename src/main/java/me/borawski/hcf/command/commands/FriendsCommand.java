package me.borawski.hcf.command.commands;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.commands.friends.FriendAcceptCommand;
import me.borawski.hcf.command.commands.friends.FriendAddCommand;
import me.borawski.hcf.command.commands.friends.FriendDeclineCommand;
import me.borawski.hcf.command.commands.friends.FriendListCommand;
import me.borawski.hcf.command.commands.friends.FriendRemoveCommand;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 3/14/2017.
 */
public class FriendsCommand extends CustomBaseCommand {

    public FriendsCommand() {
        super("friend", "Add a person as a friend.", Rank.GUEST, "friends");
        addSubCommand(new FriendAddCommand());
        addSubCommand(new FriendRemoveCommand());
        addSubCommand(new FriendAcceptCommand());
        addSubCommand(new FriendDeclineCommand());
        addSubCommand(new FriendListCommand());
    }


}
