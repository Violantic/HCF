package me.borawski.hcf.manuel.servermanuals;

import me.borawski.hcf.manuel.Manual;
import me.borawski.hcf.manuel.ManualButton;
import me.borawski.hcf.manuel.ManualPage;
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
        return new ArrayList<ManualPage>() {
            {
                add(ManualUtil.getCover(getRank()));

                // Nickname System //
                add(new ManualPage("Nick")
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
                                return new ArrayList<String>() {
                                    {
                                        add("nick start {player}");
                                    }
                                };
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
                                return new ArrayList<String>() {
                                    {
                                        add("nick remove {player}");
                                    }
                                };
                            }
                        }));
            }
        };
    }
}
