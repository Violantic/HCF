package me.borawski.hcf.frontend.command;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Session;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Ethan on 3/8/2017.
 */
public class CommandManager {

    private Core instance;
    private List<Command> commands;

    public CommandManager(Core instance) {
        this.instance = instance;
        this.commands = new ArrayList<Command>();
    }

    public Core getInstance() {
        return instance;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void add(Command c) {
        commands.add(c);
    }

    public void registerAll() {
        getCommands().stream().forEach(new Consumer<Command>() {
            @Override
            public void accept(final Command c) {
                getInstance().getCommand(c.getName()).setExecutor(new CommandExecutor() {
                    @Override
                    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
                        if(commandSender instanceof Player) {
                            UUID uuid = ((Player) commandSender).getUniqueId();
                            Session session = Session.getSession(uuid);
                            if(session.getRank().getId() < c.requiredRank().getId()) {
                                commandSender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF " + ChatColor.RESET + "" + ChatColor.GRAY + "You must be rank " + c.requiredRank().getPrefix() + ChatColor.GRAY + " to to that!");
                                return false;
                            }
                        }
                        c.execute(commandSender, strings);
                        return false;
                    }
                });
            }
        });
    }
}
