package pro.cloudnode.smp.nations.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.locale.Messages;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NationManager {
    public static @NotNull HashMap<UUID, Nation> nations = new HashMap<>();
    private final @NotNull Nations plugin;

    public NationManager(@NotNull Nations plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the nation of a player
     *
     * @param uuid The UUID of the player
     * @return The nation, or null if not found
     */
    public static @Nullable Nation getPlayerNation(@NotNull UUID uuid) {
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
     * Check if a player is in a nation
     *
     * @param uuid The UUID of the player
     * @return Whether the player is in a nation
     */
    public static boolean isInNation(@NotNull UUID uuid) {
        return getPlayerNation(uuid) != null;
    }

    /**
     * Check if a player is in a nation
     *
     * @param player The player
     * @return Whether the player is in a nation
     */
    public static boolean isInNation(@NotNull Player player) {
        return isInNation(player.getUniqueId());
    }

    public static void updatePlayersName(@NotNull UUID uuid) {
        Player player = Nations.getPlugin(Nations.class).getServer().getPlayer(uuid);
        if (player == null) return;
        Nation nation = getPlayerNation(player.getUniqueId());
        if (nation == null) {
            player.displayName(Nations.t(Messages.PLAYER_DISPLAYNAME, player));
        } else {
            player.displayName(Nations.t(Messages.NATION_DISPLAYNAME, nation, player));
        }
        player.playerListName(player.displayName());
    }

    /**
     * Add a nation
     *
     * @param nation The nation to add
     */
    public void add(Nation nation) {
        nations.put(nation.uuid, nation);
        updatePlayersName(nation.leader);
    }

    /**
     * Remove a nation
     *
     * @param nation The nation to remove
     */
    public void remove(Nation nation) {
        UUID leader = nation.leader;
        nations.remove(nation.uuid);
        updatePlayersName(leader);
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
     * <p>
     * This will overwrite the config
     * This saves into `plugins/Nations/nations.yml`
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
     * Check if a player is the leader of a nation
     *
     * @param uuid The UUID of the player
     * @return Whether the player is the leader of a nation
     */
    public boolean isLeader(UUID uuid) {
        if (!isInNation(uuid)) return false;
        return Objects.requireNonNull(getPlayerNation(uuid)).leader.equals(uuid);
    }

    /**
     * Check if a player is the leader of a nation
     *
     * @param player The player
     * @return Whether the player is the leader of a nation
     */
    public boolean isLeader(Player player) {
        if (!isInNation(player)) return false;
        return isLeader(player.getUniqueId());
    }

}
