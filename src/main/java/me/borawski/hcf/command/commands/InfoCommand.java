package me.borawski.hcf.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.gui.custom.PlayerInfoGUI;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.PlayerUtils;

/**
 * Created by Ethan on 3/8/2017.
 */
public class InfoCommand extends CustomCommand {

    public InfoCommand() {
        super("info", "Get a user's information.", Rank.ADMIN);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {

            if (args.length != 1) {
                sender.sendMessage(Core.getLangHandler().getString("usage-message").replace("{usage}", "/info [player]"));
                return;
            }

            Player player = (Player) sender;
            String name = args[0];
            Session s = Session.getSession(PlayerUtils.getUUIDFromName(name));
            if (s == null) {
                System.out.println("[Core] [ERROR] : Could not retrieve " + name);
                sender.sendMessage(ChatColor.RED + "Could not retrieve " + ChatColor.YELLOW + name);
                return;
            }
            PlayerInfoGUI.crossTarget.put(player.getUniqueId(), s);
            new PlayerInfoGUI(player).show();

        } else {
            sender.sendMessage(Core.getLangHandler().getString("only-players"));
        }
    }
}
