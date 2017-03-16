package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.backend.util.FriendUtils;
import me.borawski.hcf.backend.util.PlayerUtils;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.hcf.frontend.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 3/14/2017.
 */
public class FriendsCommand implements Command {

    private Core instance;

    public FriendsCommand(Core instance) {
        this.instance = instance;
    }

    public Core getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "friend";
    }

    @Override
    public Rank requiredRank() {
        return Rank.GUEST;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args.length == 0) {
                sendUsage((Player) sender);
                return;
            }
        } catch (Exception e) {
            // Ignore lol //
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Session playerSession = Session.getSession(player);

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("list")) {
                    if(playerSession.getFriends().size() == 0) {
                        player.sendMessage(getInstance().getPrefix() + "You have no friends");
                        return;
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "-------------------" + getInstance().getPrefix().replace(" ", "") + ChatColor.DARK_GRAY + "-----------------------");
                    int i = 0;
                    // For some reason it's duping the messages so this is a temp fix. //
                    List<UUID> used = new ArrayList<>();
                    for(UUID uuid : playerSession.getFriends()) {
                        if(used.contains(uuid)) {
                            break;
                        }
                        String status = getInstance().getServer().getPlayer(uuid)==null?ChatColor.RED + "[OFFLINE]":ChatColor.GREEN + "[ONLINE]";
                        player.sendMessage(ChatUtils.getNameWithRankColor(uuid, true) + " " + ChatColor.DARK_GRAY + "- " + status);
                        used.add(uuid);
                        i++;
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
                    return;
                }
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (args[1].equalsIgnoreCase("incoming")) {
                    if (playerSession.getIncomingRequests().size() == 0) {
                        player.sendMessage(getInstance().getPrefix() + "You have no incoming requests.");
                        return;
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "-------------------" + getInstance().getPrefix().replace(" ", "") + ChatColor.DARK_GRAY + "-----------------------");
                    int i = 0;
                    for (UUID uuid : playerSession.getIncomingRequests()) {
                        player.sendMessage(ChatColor.GRAY + "Request from: " + ChatUtils.getNameWithRankColor(uuid, true));
                        i++;
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
                    return;
                } else if (args[1].equalsIgnoreCase("outgoing")) {
                    if (playerSession.getOutgoingRequests().size() == 0) {
                        player.sendMessage(getInstance().getPrefix() + "You have no outgoing requests.");
                        return;
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "-------------------" + getInstance().getPrefix().replace(" ", "") + ChatColor.DARK_GRAY + "-----------------------");
                    int i = 0;
                    for (UUID uuid : playerSession.getOutgoingRequests()) {
                        player.sendMessage(ChatColor.GRAY + "Request sent to: " + ChatUtils.getNameWithRankColor(uuid, true));
                        i++;
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
                    return;
                }
            }

            String subusage = args[0];
            String targetName = args[1];
            if (!PlayerUtils.hasPlayed(targetName)) {
                sender.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "Player not found!");
                return;
            }

            Session target = Session.getSession(PlayerUtils.getUUIDFromName(targetName));
            String s1 = subusage.toLowerCase();
            if (s1.equals("add") || s1.equals("invite") || s1.equals("befriend")) {
                if (FriendUtils.isFriends(playerSession, target.getUUID())) {
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You are already friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false));
                    return;
                }

                if (FriendUtils.hasRequest(target, playerSession.getUUID())) {
                    // Sender has already sent a friend request to this player. //
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You have already sent this player a friend request");
                    return;
                }

                if (FriendUtils.hasRequest(playerSession, target.getUUID())) {
                    // Accept the request for the sender and receiver. //
                    FriendUtils.acceptFriendRequest(playerSession, target.getUUID(), true);
                    FriendUtils.acceptFriendRequest(target, playerSession.getUUID(), false);
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You had a friend request from " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + " so we put both of you in each others friend lists");
                    return;
                }

                if (FriendUtils.hasRequest(target, playerSession.getUUID())) {
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You've already sent a request to " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");
                    return;
                }

                if (FriendUtils.hasRequest(playerSession, target.getUUID())) {
                    FriendUtils.acceptFriendRequest(playerSession, target.getUUID(), true);
                    FriendUtils.acceptFriendRequest(target, playerSession.getUUID(), false);
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You accepted request from " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");
                    return;
                }

                FriendUtils.addFriendRequest(target, playerSession.getUUID(), true);
                FriendUtils.addFriendRequest(playerSession, target.getUUID(), false);
                player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You sent a friend request to " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");
            } else if (s1.equals("remove") || s1.equals("delete") || s1.equals("unfriend")) {
                if (!FriendUtils.isFriends(playerSession, target.getUUID())) {
                    if (FriendUtils.hasRequest(playerSession, target.getUUID())) {
                        // Deny the request for the sender and receiver //
                        FriendUtils.denyFriendRequest(playerSession, target.getUUID(), true);
                        FriendUtils.denyFriendRequest(target, playerSession.getUUID(), false);
                        player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You weren't friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + ", but you had a friend request from them so we denied it");
                        return;
                    }
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You aren't friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false));
                    return;
                }

                FriendUtils.removeFriend(playerSession, target.getUUID());
                FriendUtils.removeFriend(target, playerSession.getUUID());
                player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You removed " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + " from your friend list!");
            } else if (s1.equals("accept")) {
                if (FriendUtils.isFriends(playerSession, target.getUUID())) {
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You're already friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");
                    return;
                }

                if (!FriendUtils.hasRequest(playerSession, target.getUUID())) {
                    // Sender has not already sent a friend request to this player. //
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You don't have a request from " + ChatUtils.getNameWithRankColor(target.getUUID(), false));
                    return;
                }

                player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You accepted a request from " + ChatUtils.getNameWithRankColor(target.getUUID(), false));
                player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You are now friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");

                FriendUtils.acceptFriendRequest(playerSession, target.getUUID(), true);
                FriendUtils.acceptFriendRequest(target, playerSession.getUUID(), false);
            } else if (s1.equals("deny")) {
                if (FriendUtils.isFriends(playerSession, target.getUUID())) {
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You're already friends with " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");
                    return;
                }

                if (!FriendUtils.hasRequest(playerSession, target.getUUID())) {
                    player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You don't have a request from " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "!");
                    return;
                }

                FriendUtils.denyFriendRequest(playerSession, target.getUUID(), true);
                FriendUtils.denyFriendRequest(target, playerSession.getUUID(), false);
                player.sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You denied " + ChatUtils.getNameWithRankColor(target.getUUID(), false) + ChatColor.GRAY + "'s friend invite!");

            } else {
                sendUsage(player);
            }

            // Save data because friends is a semi-important part of the session data document //
            playerSession.dump();
            Session.getSession(player);
        }
    }

    private void sendUsage(Player player) {
        player.sendMessage(getInstance().getPrefix() + ChatColor.YELLOW + "/f add <player> " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Send a friend request");
        player.sendMessage(getInstance().getPrefix() + ChatColor.YELLOW + "/f remove <player> " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Remove a friend");
        player.sendMessage(getInstance().getPrefix() + ChatColor.YELLOW + "/f accept <player> " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Accept a request");
        player.sendMessage(getInstance().getPrefix() + ChatColor.YELLOW + "/f decline <player> " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Decline a request");
        player.sendMessage(getInstance().getPrefix() + ChatColor.YELLOW + "/f list" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "See your friend list");
        player.sendMessage(getInstance().getPrefix() + ChatColor.YELLOW + "/f list incoming" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "See recieved requests");
        player.sendMessage(getInstance().getPrefix() + ChatColor.YELLOW + "/f list outgoing" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "See sent requests");
    }

}
