package me.borawski.hcf.manuel;


import me.borawski.hcf.session.Rank;

import java.util.List;

/**
 * Created by Ethan on 5/16/2017.
 */
public interface Manual {

    Rank getRank();

    List<ManualPage> getPages();

}
