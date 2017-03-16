package me.borawski.hcf.frontend.scoreboard;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.session.Rank;
import me.borawski.hcf.backend.session.Session;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by Ethan on 3/8/2017.
 */
public class TabScoreboard {

    private Core instance;
    private Scoreboard scoreboard;

    public TabScoreboard(Core instance) {
        this.instance = instance;
        this.scoreboard = instance.getServer().getScoreboardManager().getNewScoreboard();
        for(Rank rank : Rank.values()) {
            scoreboard.registerNewTeam(rank.getDisplayName());
            if(rank == Rank.GUEST) {
                scoreboard.getTeam(rank.getDisplayName()).setPrefix(rank.getPrefix());
            } else {
                scoreboard.getTeam(rank.getDisplayName()).setPrefix(rank.getPrefix() + " ");
            }
        }
    }

    public Core getInstance() {
        return instance;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void apply(final Player player) {
        Rank r = Session.getSession(player).getRank();
        try {
            getScoreboard().getTeam(r.getDisplayName()).addPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getInstance().getServer().getScheduler().runTaskLater(getInstance(), new Runnable() {
            @Override
            public void run() {
                player.setScoreboard(getScoreboard());
            }
        }, 1L);
    }
}
