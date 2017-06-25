package me.borawski.hcf.punishment;

import java.util.UUID;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

/**
 * Created by Ethan on 3/8/2017.
 */
public class Punishment {

    @Id
    private int id;

    @Indexed
    private UUID punished;

    @Indexed
    private UUID issuer;

    private Type type;

    private long issued;

    private long expirationTime;

    private boolean repealed;

    private String reason;

    public UUID getPunished() {
        return punished;
    }

    public void setPunished(UUID punished) {
        this.punished = punished;
    }

    public UUID getIssuer() {
        return issuer;
    }

    public void setIssuer(UUID issuer) {
        this.issuer = issuer;
    }

    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }

    public long getIssued() {
        return issued;
    }

    public void setIssued(long issued) {
        this.issued = issued;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expires) {
        this.expirationTime = expires;
    }

    public boolean isRepealed() {
        return repealed;
    }

    public void setRepealed(boolean repealed) {
        this.repealed = repealed;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static enum Type {
        MUTE,
        BAN
    }

}
