package pro.cloudnode.smp.nations.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.util.Nation;

public enum Messages {
    USAGE("usage", "<aqua>Usage: <white><command> <subcommand> <args>"),
    NO_PERMISSION("no-permission", "<red>You do not have permission to use this command."),

    NATION_NOT_FOUND("nation-not-found", "<red>Nation <white><nation_name><red> not found."),

    NOT_INVITED_TO_NATION("not-invited-to-nation", "<red>You are not invited to <white><nation_name><red>"),
    JOINED_NATION("joined-nation", "<green>You have joined the nation <white><nation_name><green>"),
    NOT_IN_NATION("not-in-nation", "<red>You are not in a nation."),
    NOT_LEADER("not-leader", "<red>You are not the leader of your nation."),
    COLOR_SET("color-set", "<green>You have set your nation color to <white><color><green>"),
    INVALID_COLOR("invalid-color", "<red>Invalid color, must be a hex code or a color name."),

    INFO_HEADER("info-header", "<aqua>Info for <white><nation_name><aqua>"),
    INFO_LEADER("info-leader", "<aqua>Leader: <white><leader>"),
    INFO_MEMBERS("info-members", "<aqua>Members: <white><members>"),
    INFO_COLOR("info-color", "<aqua>Color: <white><color>"),

    LIST_HEADER("list-header", "<aqua>Nations:"),
    LIST_ITEM("list-item", "<aqua><nation_name> <gray>(<leader>)"),

    NATION_DISBANDED("nation-disbanded", "<red>Your nation has been disbanded."),

    CANT_QUIT_AS_LEADER("cant-quit-as-leader", "<red>You cannot quit the nation because you are the leader. Use <white>/nations kick <player><red> to kick a member."),

    QUIT_NATION("quit-nation", "<green>You have quit the nation <white><nation_name><green>"),

    NO_NATIONS("no-nations", "<gray>There are no nations."),
    YOU_HAVE_KICKED("you-have-kicked", "<green>You have kicked <white><player><green> from your nation."),
    YOU_HAVE_BEEN_KICKED("you-have-been-kicked", "<red>You have been kicked from <white><nation_name><red>"),
    PLAYER_NOT_FOUND("player-not-found", "<red>Player <white><player><red> not found."),
    PLAYER_NOT_IN_NATION("player-not-in-nation", "<red>Player <white><player><red> is not in your nation."),
    INVALID_NAME("invalid-name", "<red>Invalid name,  only alphanumeric characters are allowed."),
    NEW_NATION("new-nation", "<green>You have created the nation <white><nation_name><green>");

    public final String key;

    public final String default_value;

    Messages(String key, String default_value) {
        this.key = key;
        this.default_value = default_value;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return default_value;
    }

    public Component replacePlaceholders(Nation nation, Player player) {
        return MiniMessage.miniMessage().deserialize(default_value
                .replace("<nation_name>", nation.name)
                .replace("<leader>", player.getName())
                .replace("<members>", String.join(", ", nation.members.toString()))
                .replace("<color>", nation.color)
                .replace("<player>", player.getName())
                .replace("<color>", nation.color)
        );
    }

    public Component replacePlaceholders(Nation nation) {
        return MiniMessage.miniMessage().deserialize(default_value
                .replace("<nation_name>", nation.name)
                .replace("<leader>", nation.leader.toString())
                .replace("<members>", String.join(", ", nation.members.toString()))
                .replace("<color>", nation.color)
        );
    }

    public Component replacePlaceholders() {
        return MiniMessage.miniMessage().deserialize(default_value);
    }

    public Component replacePlaceholders(Player player) {
        return MiniMessage.miniMessage().deserialize(default_value
                .replace("<player>", player.getName())
        );
    }
}
