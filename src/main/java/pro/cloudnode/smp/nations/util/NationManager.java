package pro.cloudnode.smp.nations.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pro.cloudnode.smp.nations.Nations;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class NationManager {
    private final Nations plugin;
    public HashMap<UUID, Nation> nations = new HashMap<>();

    public NationManager(Nations plugin) {
        this.plugin = plugin;
    }

    public void add(Nation nation) {
        nations.put(nation.uuid, nation);
    }

    public void remove(Nation nation) {
        nations.remove(nation.uuid);
    }

    public Nation get(UUID uuid) {
        return nations.get(uuid);
    }

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

    public Nation get(String name) {
        for (Nation nation : nations.values()) {
            if (nation.name.equals(name)) {
                return nation;
            }
        }
        return null;
    }

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
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(this.plugin.getDataFolder(), "nations.yml");
        if (file.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (config.getConfigurationSection("nations") != null) {
                config.getConfigurationSection("nations").getKeys(false).forEach(key -> {
                    add(new Nation(key, config));
                });
            }
        }
    }

}
