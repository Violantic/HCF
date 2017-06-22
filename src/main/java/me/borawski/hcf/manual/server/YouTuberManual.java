package me.borawski.hcf.manual.server;

import me.borawski.hcf.manual.Manual;
import me.borawski.hcf.manual.ManualButton;
import me.borawski.hcf.manual.ManualPage;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.ManualUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 5/16/2017.
 */
public class YouTuberManual implements Manual {

    @Override
    public Rank getRank() {
        return Rank.YOUTUBER;
    }

    @Override
    public List<ManualPage> getPages() {
        ArrayList<ManualPage> pages = new ArrayList<>();
        pages.add(ManualUtil.getCover(getRank()));
        pages.add(new ManualPage("Nick")
                .addString("&4&lNICK SYSTEM")
                .addString("\n")
                .addString("\n" + "&7Here you can")
                .addString("\n" + "&7Change your")
                .addString("\n" + "&7identity")
                .addString("\n")
                .addButton(new ManualButton() {
                    @Override
                    public String getName() {
                        return "[Set Nick]";
                    }

                    @Override
                    public ChatColor getColor() {
                        return ChatColor.GREEN;
                    }

                    @Override
                    public List<String> getCommands() {
                        ArrayList<String> commands = new ArrayList<>();
                        commands.add("nick start {player}");
                        return commands;
                    }
                })
                .addButton(new ManualButton() {
                    @Override
                    public String getName() {
                        return "[No Nick]";
                    }

                    @Override
                    public ChatColor getColor() {
                        return ChatColor.RED;
                    }

                    @Override
                    public List<String> getCommands() {
                        ArrayList<String> commands = new ArrayList<>();
                        commands.add("nick remove {player}");
                        return commands;
                    }
                }));
        return pages;
    }
}
