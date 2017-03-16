package me.borawski.hcf.frontend.gui.custom.edit;

import me.borawski.hcf.Core;
import me.borawski.hcf.backend.connection.Mongo;
import me.borawski.hcf.backend.punishment.Punishment;
import me.borawski.hcf.backend.session.Session;
import me.borawski.hcf.frontend.gui.CustomIS;
import me.borawski.hcf.frontend.gui.ItemGUI;
import me.borawski.hcf.frontend.gui.MenuItem;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.ocpsoft.prettytime.PrettyTime;


/**
 * Created by Ethan on 3/8/2017.
 */
public class PlayerPunishmentsGUI extends ItemGUI {

    private Session target;
    private PrettyTime timeFormat;
    public PlayerPunishmentsGUI(Core instance, Player p, Session target) {
        super(instance, null, p, 9);
        this.target = target;
        this.timeFormat = new PrettyTime();
    }

    public ItemGUI issuePunishment(final Session session, final Punishment.Type type) {
        return new ItemGUI(getInstance(), null, getPlayer(), 36) {
            @Override
            public String getName() {
                return "Punish";
            }

            @Override
            public boolean isCloseOnClick() {
                return false;
            }

            @Override
            public void registerItems() {
                if(session.isBanned() && type == Punishment.Type.BAN) {
                    getPlayer().sendMessage(ChatColor.RED + "That player is already banned!");
                    return;
                } else if(session.isMuted() && type == Punishment.Type.MUTE) {
                    getPlayer().sendMessage(ChatColor.RED + "That player is already muted!");
                    return;
                }
                set(0, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("30 Minutes"), new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getPunishmentManager().issue(type, session.getUUID(), getPlayer().getUniqueId(), 1800000l, "Staff issued punishment");
                    }
                }));
                set(1, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("1 Hour"), new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getPunishmentManager().issue(type, session.getUUID(), getPlayer().getUniqueId(), 3600000l, "Staff issued punishment");
                    }
                }));
                set(2, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("2 Hours"), new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getPunishmentManager().issue(type, session.getUUID(), getPlayer().getUniqueId(), 7200000l, "Staff issued punishment");
                    }
                }));
                set(3, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("1 Day"), new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getPunishmentManager().issue(type, session.getUUID(), getPlayer().getUniqueId(), 86400000l, "Staff issued punishment");
                    }
                }));
                set(4, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("2 Days"), new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getPunishmentManager().issue(type, session.getUUID(), getPlayer().getUniqueId(), 172800000l, "Staff issued punishment");
                    }
                }));
                set(5, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("7 Days"), new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getPunishmentManager().issue(type, session.getUUID(), getPlayer().getUniqueId(), 604800000l, "Staff issued punishment");
                    }
                }));
                set(6, new MenuItem(new CustomIS().setMaterial(Material.LAVA_BUCKET).setName("Permanent"), new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getPunishmentManager().issue(type, session.getUUID(), getPlayer().getUniqueId(), -1, "Staff issued punishment");
                    }
                }));
            }
        };
    }

    @Override
    public String getName() {
        return "Punishments";
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        set(0, new MenuItem(new CustomIS().setMaterial(Material.BOOK).setName("BANNED: " + ChatColor.GREEN + " False").addLore(ChatColor.GRAY + "(Click to see Ban history)"), new Runnable() {
            @Override
            public void run() {
            }
        }));
        set(1, new MenuItem(new CustomIS().setMaterial(Material.BOOK).setName("MUTED: " + ChatColor.GREEN + " False").addLore(ChatColor.GRAY + "(Click to see Mute history)"), new Runnable() {
            @Override
            public void run() {
            }
        }));
        set(4, new MenuItem(new CustomIS().setMaterial(Material.BOOK_AND_QUILL).setName("ISSUE PUNISHMENT").addLore(ChatColor.GRAY + "(Click to issue a punishment)"), new Runnable() {
            @Override
            public void run() {
                new ItemGUI(PlayerPunishmentsGUI.this.getInstance(), null, PlayerPunishmentsGUI.this.getPlayer(), 36) {
                    @Override
                    public String getName() {
                        return "Select";
                    }

                    @Override
                    public boolean isCloseOnClick() {
                        return false;
                    }

                    @Override
                    public void registerItems() {
                        set(0, new MenuItem(new CustomIS().setMaterial(Material.PAPER).setName("MUTE").addLore(ChatColor.GRAY + "(Click to issue a mute)"), new Runnable() {
                            @Override
                            public void run() {
                                issuePunishment(target, Punishment.Type.MUTE).show();
                            }
                        }));
                        set(8, new MenuItem(new CustomIS().setMaterial(Material.PAPER).setName("BAN").addLore(ChatColor.GRAY + "(Click to issue a ban)"), new Runnable() {
                            @Override
                            public void run() {
                                issuePunishment(target, Punishment.Type.BAN).show();
                            }
                        }));
                    }
                }.show();
            }
        }));
        set(7, new MenuItem(new CustomIS().setMaterial(Material.BARRIER).setName("REVOKE BAN").addLore(ChatColor.GRAY + "(Click to remove current ban)"), new Runnable() {
            @Override
            public void run() {
                if(!target.isBanned()) {
                    getPlayer().sendMessage(ChatColor.RED + "That player isn't banned!");
                    return;
                }
                Mongo.getCollection("punishments").deleteOne(new Document("uuid", target.getUUID().toString()));
                getPlayer().sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You unbanned " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "!");

            }
        }));
        set(8, new MenuItem(new CustomIS().setMaterial(Material.BARRIER).setName("REVOKE MUTE").addLore(ChatColor.GRAY + "(Click to remove current mute)"), new Runnable() {
            @Override
            public void run() {
                if(!target.isMuted()) {
                    getPlayer().sendMessage(ChatColor.RED + "That player isn't muted!");
                    return;
                }
                Mongo.getCollection("punishments").deleteOne(new Document("uuid", target.getUUID().toString()));
                getPlayer().sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You un-muted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "!");
            }
        }));
    }
}
