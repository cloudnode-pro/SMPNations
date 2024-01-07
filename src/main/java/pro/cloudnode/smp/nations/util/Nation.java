package pro.cloudnode.smp.nations.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Nation {
    public @NotNull UUID uuid;
    public @NotNull String name;
    public @NotNull UUID leader;
    public @NotNull List<UUID> members;
    public @NotNull List<UUID> invited;
    public @NotNull String color;

    public Nation(@NotNull UUID uuid, @NotNull String name, @NotNull UUID leader, @NotNull List<UUID> members, @NotNull List<UUID> invited, @NotNull String color) {
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
        this.members = List.of(new UUID[0]);
        this.invited = List.of(new UUID[0]);
        this.color = "white";
    }

    public Nation(String key, FileConfiguration config) {
        this.uuid = UUID.fromString(key);
        this.load((YamlConfiguration) config);
    }

    public void save(YamlConfiguration config) {
        config.set("nations." + uuid + ".name", name);
        config.set("nations." + uuid + ".leader", leader.toString());
        config.set("nations." + uuid + ".members", members);
        config.set("nations." + uuid + ".invited", invited);
        config.set("nations." + uuid + ".color", color);
    }

    public void load(YamlConfiguration config) {
        this.name = Objects.requireNonNull(config.getString("nations." + uuid + ".name"));
        this.leader = UUID.fromString(Objects.requireNonNull(config.getString("nations." + uuid + ".leader")));
        this.members = (List<UUID>) Objects.requireNonNull(config.getList("nations." + uuid + ".members"));
        this.invited = (List<UUID>) Objects.requireNonNull(config.getList("nations." + uuid + ".invited"));
        this.color = Objects.requireNonNull(config.getString("nations." + uuid + ".color"));
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public void addInvited(UUID uuid) {
        invited.add(uuid);
    }

    public void removeInvited(UUID uuid) {
        invited.remove(uuid);
    }


}
