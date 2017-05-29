package com.massivecraft.factions.struct;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.zcore.util.TL;

public enum Role {
    ADMIN(2, TL.ROLE_ADMIN),
    MODERATOR(1, TL.ROLE_MODERATOR),
    NORMAL(0, TL.ROLE_NORMAL);

    public final int value;
    public final String nicename;
    public final TL translation;

    private Role(final int value, final TL translation) {
        this.value = value;
        this.nicename = translation.toString();
        this.translation = translation;
    }

    public boolean isAtLeast(com.massivecraft.factions.struct.Role role) {
        return this.value >= role.value;
    }

    public boolean isAtMost(com.massivecraft.factions.struct.Role role) {
        return this.value <= role.value;
    }

    @Override
    public String toString() {
        return this.nicename;
    }

    public TL getTranslation(){
        return translation;
    }

    public String getPrefix() {
        if (this == ADMIN) {
            return Conf.prefixAdmin;
        }

        if (this == MODERATOR) {
            return Conf.prefixMod;
        }

        return "";
    }
}
