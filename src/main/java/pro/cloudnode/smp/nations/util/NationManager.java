package pro.cloudnode.smp.nations.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.locale.Messages;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NationManager {
    private final Nations plugin;
    public static HashMap<UUID, Nation> nations = new HashMap<>();

    public NationManager(Nations plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a nation
     *
     * @param nation The nation to add
     */
    public void add(Nation nation) {
        nations.put(nation.uuid, nation);
        updatePlayersDisplayname(nation.leader);
    }

    /**
     * Remove a nation
     *
     * @param nation The nation to remove
     */
    public void remove(Nation nation) {
        UUID leader = nation.leader;
        nations.remove(nation.uuid);
        updatePlayersDisplayname(leader);
    }

    /**
     * Get a nation by UUID
     *
     * @param uuid The UUID of the nation
     * @return The nation, or null if not found
     */
    public Nation get(UUID uuid) {
        return nations.get(uuid);
    }

    /**
     * Get the nation of a player
     *
     * @param uuid The UUID of the player
     * @return The nation, or null if not found
     */
    public static Nation getPlayerNation(UUID uuid) {
        for (Nation nation : nations.values()) {
            if (nation.leader.equals(uuid)) {
                return nation;
            }
            if (nation.members.contains(uuid)) {
                return nation;
            }
        }
        return null;
    }

    /**
     * Get a nation by name
     *
     * @param name The name of the nation
     * @return The nation, or null if not found
     */
    public Nation get(String name) {
        for (Nation nation : nations.values()) {
            if (nation.name.equals(name)) {
                return nation;
            }
        }
        return null;
    }

    /**
     * Save nations to the config
     *
     * @implNote This will overwrite the config
     * @implNote This saves into `plugins/Nations/nations.yml`
     */
    public void save() {
        // save nations
        YamlConfiguration config = new YamlConfiguration();
        if (!nations.isEmpty()) {
            nations.forEach((uuid, nation) -> {
                nation.save(config);
            });
        }

        // write to plugins/Nations/nations.yml
        try {
            File file = new File(this.plugin.getDataFolder(), "nations.yml");
            config.save(file);
        } catch (Exception e) {
            this.plugin.getLogger().warning("Failed to save nations.yml");
            this.plugin.getLogger().warning(e.getMessage());
        }
    }

    public List<String> getNations() {
        return nations.values().stream().map(nation -> nation.name).toList();
    }

    public List<String> getNationsInviting(UUID uuid) {
        return nations.values().stream().filter(nation -> nation.invited.contains(uuid)).map(nation -> nation.name).toList();
    }

    /**
     * Load nations from the config
     */
    public void load() {
        File file = new File(this.plugin.getDataFolder(), "nations.yml");
        if (file.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (config.getConfigurationSection("nations") != null) {
                Objects.requireNonNull(config.getConfigurationSection("nations")).getKeys(false).forEach(key -> {
                    add(new Nation(key, config));
                });
            }
        }
    }

    /**
     * Check if a player is in a nation
     * @param uuid The UUID of the player
     * @return Whether the player is in a nation
     */
    public static boolean isInNation(UUID uuid) {
        return getPlayerNation(uuid) != null;
    }

    /**
     * Check if a player is the leader of a nation
     * @param uuid The UUID of the player
     * @return Whether the player is the leader of a nation
     */
    public boolean isLeader(UUID uuid) {
        if (!isInNation(uuid)) return false;
        return Objects.requireNonNull(getPlayerNation(uuid)).leader.equals(uuid);
    }

    /**
     * Check if a player is in a nation
     * @param player The player
     * @return Whether the player is in a nation
     */
    public static boolean isInNation(Player player) {
        return isInNation(player.getUniqueId());
    }

    /**
     * Check if a player is the leader of a nation
     * @param player The player
     * @return Whether the player is the leader of a nation
     */
    public boolean isLeader(Player player) {
        if (!isInNation(player)) return false;
        return isLeader(player.getUniqueId());
    }

    public static void updatePlayersDisplayname(UUID uuid) {
        Player player = Nations.getPlugin(Nations.class).getServer().getPlayer(uuid);
        if (player == null) return;
        if (isInNation(player)) {
            Nation nation = getPlayerNation(player.getUniqueId());
            player.displayName(Nations.t(Messages.NATION_DISPLAYNAME, nation, player));
        } else {
            player.displayName(Nations.t(Messages.PLAYER_DISPLAYNAME, player));
        }
        player.playerListName(player.displayName());
    }

}
