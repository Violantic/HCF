package me.borawski.hcf.session;

import me.borawski.hcf.Core;
import me.borawski.hcf.connection.Mongo;
import me.borawski.hcf.util.BarUtil;
import me.borawski.hcf.util.ChatUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Ethan on 3/12/2017.
 */
public class Session {

    private static List<Session> cache = new ArrayList<>();

    private UUID uuid;
    private Document playerDocument;
    private String name;
    private Rank rank;
    private int tokens;
    private int level;
    private int exp;
    private boolean muted;
    private boolean banned;
    private List<String> achievements;
    private List<UUID> friends;
    private List<List<UUID>> friendRequests;
    private Map<String, String> settings;

    @SuppressWarnings("unchecked")
    public Session(UUID uuid, Document playerDocument, String name) {
        this.uuid = uuid;
        if (playerDocument == null && name != null) {
            playerDocument = new Document("uuid", uuid.toString());
            playerDocument.append("name", name);
            playerDocument.append("rank", Rank.GUEST.toString());
            playerDocument.append("tokens", 0);
            playerDocument.append("level", 1);
            playerDocument.append("exp", 0);
            playerDocument.append("first_login", System.currentTimeMillis());
            playerDocument.append("last_login", System.currentTimeMillis());
            playerDocument.append("last_logout", 0);
            playerDocument.append("ip", "10.0.0.1");
            playerDocument.append("achievements", new ArrayList<String>());
            playerDocument.append("friends", new ArrayList<UUID>());

            ArrayList<List<UUID>> friendRequests = new ArrayList<>();
            friendRequests.add(new ArrayList<UUID>());
            friendRequests.add(new ArrayList<UUID>());
            playerDocument.append("friend_requests", friendRequests);

            HashMap<String, Object> settings = new HashMap<>();
            settings.put("friend_requests", "true");
            settings.put("private_messaging", "false");
            playerDocument.append("settings", settings);

            Mongo.getCollection("players").insertOne(playerDocument);
            this.name = name;
            getSession(uuid);
        } else if (playerDocument != null) {
            this.playerDocument = playerDocument;
            this.name = playerDocument.getString("name");
            this.rank = Rank.valueOf(playerDocument.getString("rank"));
            this.tokens = playerDocument.getInteger("tokens");
            this.level = playerDocument.getInteger("level");
            this.exp = playerDocument.getInteger("exp");
            this.achievements = playerDocument.get("achievements", ArrayList.class);
            this.friends = playerDocument.get("friends", ArrayList.class);
            this.friendRequests = playerDocument.get("friend_requests", ArrayList.class);
            this.settings = playerDocument.get("settings", Map.class);

            Document document = Mongo.getCollection("punishments").find(new Document("uuid", uuid.toString())).first();
            if (document != null) {
                System.out.println("[DesireHCF] Found punishment document for: " + uuid.toString());
                System.out.println("[DesireHCF] Punishment Type: " + document.getString("type"));
                if (document.getString("type").equalsIgnoreCase("MUTE")) {
                    this.muted = true;
                } else if (document.getString("type").equalsIgnoreCase("BAN")) {
                    this.banned = true;
                }
            } else {
                System.out.println("[DesireHCF] Punishment document was not found for: " + uuid.toString());
            }

            updateDocument("players", "last_login", System.currentTimeMillis());
            cache.add(this);
        }
    }

    /*
     * Getters
     */

    public static Session getSession(Player player) {
        for (int i = 0, cacheSize = cache.size(); i < cacheSize; i++) {
            Session session = cache.get(i);
            System.out.println(session.getUUID());
            if (session.getUUID().equals(player.getUniqueId())) {
                return session;
            }
        }

        return new Session(player.getUniqueId(), Mongo.getCollection("players").find(new Document("uuid", player.getUniqueId().toString())).first(), player.getName());
    }

    public static Session getSession(UUID player) {
        for (int i = 0, cacheSize = cache.size(); i < cacheSize; i++) {
            Session session = cache.get(i);
            if (session.getUUID().equals(player)) {
                return session;
            }
        }

        return new Session(player, Mongo.getCollection("players").find(new Document("uuid", player.toString())).first(), null);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Document getPlayerDocument() {
        return this.playerDocument;
    }

    public String getName() {
        return name;
    }

    public Rank getRank() {
        return this.rank;
    }

    public int getTokens() {
        return this.tokens;
    }

    public int getLevel() {
        return this.level;
    }

    public int getExp() {
        return this.exp;
    }

    public boolean isMuted() {
        return this.muted;
    }

    public boolean isBanned() {
        return this.banned;
    }

    public List<String> getAchievements() {
        return this.achievements;
    }

    public boolean hasAchievement(String id) {
        return getAchievements().contains(id);
    }

    public List<UUID> getFriends() {
        return this.friends;
    }

    public List<UUID> getOutgoingRequests() {
        return this.friendRequests.get(0);
    }

    public List<UUID> getIncomingRequests() {
        return this.friendRequests.get(1);
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    /*
     * Methods
     */

    public void dump() {
        cache.remove(this);
    }

    public void sendMessage(String message) {
        Core.getInstance().getServer().getPlayer(getUUID()).sendMessage(message);
    }

    public void awardTokens(int tokens, boolean inform) {
        int newTokens = (getTokens() + tokens);
        this.tokens = newTokens;
        updateDocument("players", "tokens", getTokens());

        if (inform)
            sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You have been awarded "
                    + ChatColor.YELLOW + tokens + ChatColor.GRAY + " tokens!");
    }

    public void addEXP(final int exp, boolean inform) {
        int newExp = (getExp() + exp);
        this.exp = newExp;
        updateDocument("players", "exp", getExp());

        int expForNextLevel = (getLevel() * 1000 / 2);

        if (getExp() >= expForNextLevel) {
            this.exp = 0;
            this.level = getLevel() + 1;

            updateDocument("players", "level", getLevel());
            updateDocument("players", "exp", getExp());

            sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
            sendMessage("");
            ChatUtils.sendCenteredMessage(Bukkit.getServer().getPlayer(getUUID()), ChatColor.GREEN + "" + ChatColor.BOLD + "LEVEL UP");
            sendMessage("");
            ChatUtils.sendCenteredMessage(Bukkit.getServer().getPlayer(getUUID()), ChatColor.GRAY + "You're now Level " + ChatColor.YELLOW + getLevel() + ChatColor.GRAY + "!");
            ChatUtils.sendCenteredMessage(Bukkit.getServer().getPlayer(getUUID()), ChatColor.GRAY + "Reward: " + ChatColor.YELLOW + "A Dank Meme");
            sendMessage("");
            sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        }

        if (inform) {
            BarUtil.sendActionBar(Bukkit.getServer().getPlayer(getUUID()),
                    ChatColor.GREEN + "" + ChatColor.BOLD + "+ " + exp + " EXP" + ChatColor.DARK_GRAY + " | " + ChatColor.WHITE + "("
                            + getExp() + "/" + (getLevel() * 1000 / 2) + ")");
        }
    }

    public void awardAchievement(Achievement achievement, boolean inform) {
        if (hasAchievement(achievement.getId())) return;

        getAchievements().add(achievement.getId());
        updateDocument("players", "achievements", getAchievements());

        if (inform) {
            Player player = Core.getInstance().getServer().getPlayer(getUUID());
            sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
            ChatUtils.sendCenteredMessage(player, Core.getInstance().getPrefix() + ChatColor.GRAY + "Achievement unlocked!");
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "Name: " + ChatColor.YELLOW + achievement.getName());
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "Desc: " + ChatColor.YELLOW + achievement.getDesc());
            if (achievement.getReward() > 0) {
                awardTokens(achievement.getReward(), false);
                ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "Reward: " + ChatColor.YELLOW + achievement.getReward() + " Tokens!");
            }
            sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        }
    }

    @SuppressWarnings("unchecked")
    public void updateDocument(String collection, String key, Object value) {
        Core.getInstance().getServer().getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Document document = getPlayerDocument().append(key, value);
                Mongo.getCollection(collection).findOneAndReplace(new Document("uuid", getUUID().toString()), document);
            }
        });

        this.playerDocument = Mongo.getCollection(collection).find(new Document("uuid", getUUID().toString())).first();
        this.rank = Rank.valueOf(getPlayerDocument().getString("rank"));
        this.tokens = getPlayerDocument().getInteger("tokens");
        this.level = getPlayerDocument().getInteger("level");
        this.exp = getPlayerDocument().getInteger("exp");
        this.achievements = playerDocument.get("achievements", ArrayList.class);
        this.friends = playerDocument.get("friends", ArrayList.class);
        this.friendRequests = playerDocument.get("friend_requests", ArrayList.class);
        this.settings = playerDocument.get("settings", Map.class);
    }

}
