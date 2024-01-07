package pro.cloudnode.smp.nations.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Nation {
    public @NotNull UUID uuid;
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

    public Nation(String key, FileConfiguration config) {
        this.uuid = UUID.fromString(key);
        this.load((YamlConfiguration) config);
    }

    public void save(YamlConfiguration config) {
        config.set("nations." + uuid + ".name", name);
        config.set("nations." + uuid + ".leader", leader.toString());
        config.set("nations." + uuid + ".members", Arrays.stream(members.toArray()).map(Object::toString).toArray());
        config.set("nations." + uuid + ".invited", Arrays.stream(invited.toArray()).map(Object::toString).toArray());
        config.set("nations." + uuid + ".color", color);
    }

    public void load(YamlConfiguration config) {
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
