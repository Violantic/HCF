package me.borawski.hcf.frontend.command.commands;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.backend.util.PlayerUtils;
import me.borawski.hcf.frontend.command.Command;
import me.borawski.hcf.frontend.gui.custom.PlayerInfoGUI;
import me.borawski.hcf.frontend.gui.custom.edit.PlayerPunishmentsGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 3/8/2017.
 */
public class InfoCommand implements Command {

    private Core instance;
    public InfoCommand(Core instance) {
        this.instance = instance;
    }

    public Core getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public Rank requiredRank() {
        return Rank.ADMIN;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments: /info <player>");
            } else if (args.length == 1) {
                String name = args[0];
                Session s = Session.getSession(PlayerUtils.getUUIDFromName(name));
                if(s == null) {
                    System.out.println("[Core] [ERROR] : Could not retrieve " + name);
                    sender.sendMessage(ChatColor.RED + "Could not retrieve " + ChatColor.YELLOW + name);
                    return;
                }
                PlayerInfoGUI.crossTarget.put(player.getUniqueId(), s);
                new PlayerInfoGUI(getInstance(), player).show();
            }
        }
    }
}
