package pro.cloudnode.smp.nations.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pro.cloudnode.smp.nations.Nations;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class NationManager {
    private final Nations plugin;
    public HashMap<UUID, Nation> nations = new HashMap<>();

    public NationManager(Nations plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a nation
     * @param nation The nation to add
     */
    public void add(Nation nation) {
        nations.put(nation.uuid, nation);
    }

    /**
     * Remove a nation
     * @param nation The nation to remove
     */
    public void remove(Nation nation) {
        nations.remove(nation.uuid);
    }

    /**
     * Get a nation by UUID
     * @param uuid The UUID of the nation
     * @return The nation, or null if not found
     */
    public Nation get(UUID uuid) {
        return nations.get(uuid);
    }

    /**
     * Get the nation of a player
     * @param uuid The UUID of the player
     * @return The nation, or null if not found
     */
    public Nation getPlayerNation(UUID uuid) {
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

}
