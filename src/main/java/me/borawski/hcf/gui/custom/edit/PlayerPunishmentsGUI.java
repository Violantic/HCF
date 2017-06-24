package me.borawski.hcf.gui.custom.edit;

import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.connection.Mongo;
import me.borawski.hcf.gui.CustomIS;
import me.borawski.hcf.gui.ItemGUI;
import me.borawski.hcf.gui.MenuItem;
import me.borawski.hcf.gui.custom.PlayerInfoGUI;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.session.Session;

/**
 * Created by Ethan on 3/8/2017.
 */
public class PlayerPunishmentsGUI extends ItemGUI {

    private Session target;

    public PlayerPunishmentsGUI(Player p, Session target) {
        super(null, p, 9);
        this.target = PlayerInfoGUI.crossTarget.get(p.getUniqueId());
    }

    public ItemGUI issuePunishment(final Session session, final Punishment.Type type) {
        return new ItemGUI(null, getPlayer(), 36) {
            @Override
            public String getName() {
                return "Punish";
            }

            @Override
            public boolean isCloseOnClick() {
                return false;
            }

            @SuppressWarnings("deprecation")
            @Override
            public void registerItems() {
                if (session.isBanned() != null && type == Punishment.Type.BAN) {
                    getPlayer().sendMessage(ChatColor.RED + "That player is already banned!");
                    return;
                } else if (session.isMuted() != null && type == Punishment.Type.MUTE) {
                    getPlayer().sendMessage(ChatColor.RED + "That player is already muted!");
                    return;
                }
                set(0, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("30 Minutes"), new Runnable() {
                    @Override
                    public void run() {
                        Core.getInstance().getPunishmentHandler().issuePunishment(type, session.getUniqueId(), getPlayer().getUniqueId(), 1800000l, "Staff issued punishment");
                    }
                }));
                set(1, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("1 Hour"), new Runnable() {
                    @Override
                    public void run() {
                        Core.getInstance().getPunishmentHandler().issuePunishment(type, session.getUniqueId(), getPlayer().getUniqueId(), 3600000l, "Staff issued punishment");
                    }
                }));
                set(2, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("2 Hours"), new Runnable() {
                    @Override
                    public void run() {
                        Core.getInstance().getPunishmentHandler().issuePunishment(type, session.getUniqueId(), getPlayer().getUniqueId(), 7200000l, "Staff issued punishment");
                    }
                }));
                set(3, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("1 Day"), new Runnable() {
                    @Override
                    public void run() {
                        Core.getInstance().getPunishmentHandler().issuePunishment(type, session.getUniqueId(), getPlayer().getUniqueId(), 86400000l, "Staff issued punishment");
                    }
                }));
                set(4, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("2 Days"), new Runnable() {
                    @Override
                    public void run() {
                        Core.getInstance().getPunishmentHandler().issuePunishment(type, session.getUniqueId(), getPlayer().getUniqueId(), 172800000l, "Staff issued punishment");
                    }
                }));
                set(5, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getDyeData()).setName("7 Days"), new Runnable() {
                    @Override
                    public void run() {
                        Core.getInstance().getPunishmentHandler().issuePunishment(type, session.getUniqueId(), getPlayer().getUniqueId(), 604800000l, "Staff issued punishment");
                    }
                }));
                set(6, new MenuItem(new CustomIS().setMaterial(Material.LAVA_BUCKET).setName("Permanent"), new Runnable() {
                    @Override
                    public void run() {
                        Core.getInstance().getPunishmentHandler().issuePunishment(type, session.getUniqueId(), getPlayer().getUniqueId(), -1, "Staff issued punishment");
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
        set(0, new MenuItem(new CustomIS().setMaterial(Material.BOOK).setName("BANNED: " + ((PlayerInfoGUI.crossTarget.get(getPlayer().getUniqueId()).isBanned() == null) ? ChatColor.RED + "False" : ChatColor.GREEN + "True")).addLore(ChatColor.GRAY + "(Click to see Ban history)"), new Runnable() {
            @Override
            public void run() {
                PlayerInfoGUI.crossTarget.remove(getPlayer().getUniqueId());
            }
        }));
        set(1, new MenuItem(new CustomIS().setMaterial(Material.BOOK).setName("MUTED: " + ((PlayerInfoGUI.crossTarget.get(getPlayer().getUniqueId()).isMuted() == null) ? ChatColor.RED + "False" : ChatColor.GREEN + "True")).addLore(ChatColor.GRAY + "(Click to see Mute history)"), new Runnable() {
            @Override
            public void run() {
                PlayerInfoGUI.crossTarget.remove(getPlayer().getUniqueId());
            }
        }));
        set(4, new MenuItem(new CustomIS().setMaterial(Material.BOOK_AND_QUILL).setName("ISSUE PUNISHMENT").addLore(ChatColor.GRAY + "(Click to issue a punishment)"), new Runnable() {
            @Override
            public void run() {
                new ItemGUI(null, PlayerPunishmentsGUI.this.getPlayer(), 36) {
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
                if (target.isBanned() == null) {
                    getPlayer().sendMessage(ChatColor.RED + "That player isn't banned!");
                    return;
                }
                Mongo.getCollection("punishments").deleteOne(new Document("uuid", target.getUniqueId().toString()));
                getPlayer().sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You unbanned " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "!");
                target.dump();
            }
        }));
        set(8, new MenuItem(new CustomIS().setMaterial(Material.BARRIER).setName("REVOKE MUTE").addLore(ChatColor.GRAY + "(Click to remove current mute)"), new Runnable() {
            @Override
            public void run() {
                if (target.isMuted() == null) {
                    getPlayer().sendMessage(ChatColor.RED + "That player isn't muted!");
                    return;
                }
                Mongo.getCollection("punishments").deleteOne(new Document("uuid", target.getUniqueId().toString()));
                getPlayer().sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You un-muted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "!");
                target.dump();
            }
        }));
    }
}
