package pro.cloudnode.smp.nations.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Nation {
    public final @NotNull UUID uuid;
    public @NotNull String name;
    public @NotNull UUID leader;
    public @NotNull ArrayList<UUID> members;
    public @NotNull ArrayList<UUID> invited;
    public @NotNull String color;

    public Nation(@NotNull UUID uuid, @NotNull String name, @NotNull UUID leader, @NotNull ArrayList<UUID> members, @NotNull ArrayList<UUID> invited, @NotNull String color) {
        this.uuid = uuid;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.invited = invited;
        this.color = color;
    }

    public Nation(@NotNull String name, @NotNull UUID leader) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.leader = leader;
        this.members = new ArrayList<>();
        this.invited = new ArrayList<>();
        this.color = "white";
    }

    public Nation(@NotNull String key, @NotNull FileConfiguration config) {
        this.uuid = UUID.fromString(key);
        this.load((YamlConfiguration) config);
    }

    /**
     * Save the nation to a config
     *
     * @param config The config to save to
     */
    public void save(@NotNull YamlConfiguration config) {
        config.set("nations." + uuid + ".name", name);
        config.set("nations." + uuid + ".leader", leader.toString());
        config.set("nations." + uuid + ".members", Arrays.stream(members.toArray()).map(Object::toString).toArray());
        config.set("nations." + uuid + ".invited", Arrays.stream(invited.toArray()).map(Object::toString).toArray());
        config.set("nations." + uuid + ".color", color);
    }

    /**
     * Load a nation from the config
     *
     * @param config The config to load from
     */
    public void load(@NotNull YamlConfiguration config) {
        this.name = Objects.requireNonNull(config.getString("nations." + uuid + ".name"));
        this.leader = UUID.fromString(Objects.requireNonNull(config.getString("nations." + uuid + ".leader")));
        this.members = new ArrayList<>();
        this.invited = new ArrayList<>();

        String[] members = config.getStringList("nations." + uuid + ".members").toArray(new String[0]);
        String[] invited = config.getStringList("nations." + uuid + ".invited").toArray(new String[0]);

        for (String member : members) {
            this.members.add(UUID.fromString(member));
        }

        for (String invite : invited) {
            this.invited.add(UUID.fromString(invite));
        }

        this.color = Objects.requireNonNull(config.getString("nations." + uuid + ".color"));
    }

    /**
     * Add a member to the nation
     *
     * @param uuid The UUID of the player to add
     */
    public void addMember(@NotNull UUID uuid) {
        members.add(uuid);
        update();
    }

    /**
     * Remove a member from the nation
     *
     * @param uuid The UUID of the player to remove
     */
    public void removeMember(@NotNull UUID uuid) {
        members.remove(uuid);
        update();
    }

    /**
     * Add an invited player
     *
     * @param uuid The UUID of the player to add
     * This adds a player to a 'invited list', and does not add them to a nation
     */
    public void addInvited(@NotNull UUID uuid) {
        invited.add(uuid);
    }

    /**
     * Remove an invited player
     *
     * @param uuid The UUID of the player to remove
     */
    public void removeInvited(@NotNull UUID uuid) {
        invited.remove(uuid);
    }


    public void update() {
        // update leader
        NationManager.updatePlayersName(leader);

        // update members
        for (UUID member : members) {
            NationManager.updatePlayersName(member);
        }
    }
}
