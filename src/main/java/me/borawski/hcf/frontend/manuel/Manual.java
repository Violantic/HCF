package me.borawski.hcf.frontend.manuel;

import me.borawski.hcf.backend.session.Rank;

import java.util.List;

/**
 * Created by Ethan on 5/16/2017.
 */
public interface Manual {

    Rank getRank();

    List<ManualPage> getPages();

}
