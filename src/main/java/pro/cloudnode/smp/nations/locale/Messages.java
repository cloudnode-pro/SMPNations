package pro.cloudnode.smp.nations.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.util.Nation;

public enum Messages {
    USAGE("usage", "<aqua>Usage: <white>/$0 $1 <gray>$2"),
    NO_PERMISSION("no-permission", "<red>You do not have permission to use this command."),

    NATION_NOT_FOUND("nation-not-found", "<red>Nation <white>$0<red> not found."),

    NOT_INVITED_TO_NATION("not-invited-to-nation", "<red>You are not invited to <white><nation_name><red>"),
    JOINED_NATION("joined-nation", "<aqua>You have joined the nation <white><nation_name><aqua>"),
    NOT_IN_NATION("not-in-nation", "<red>You are not in a nation."),
    NOT_LEADER("not-leader", "<red>You are not the leader of your nation."),
    COLOR_SET("color-set", "<aqua>You have set your nation color to <$0>$0<aqua>"),
    INVALID_COLOR("invalid-color", "<red>Invalid color, must be a hex code or a color name."),

    INFO_HEADER("info-header", "<aqua>Info for <nation_color><nation_name><aqua>"),
    INFO_LEADER("info-leader", "<aqua>Leader: <white>$0"),
    INFO_MEMBERS("info-members", "<aqua>Members: <white>$0"),
    INFO_COLOR("info-color", "<aqua>Color: <white><color>"),

    LIST_HEADER("list-header", "<aqua>Nations:"),
    //@todo: add hover information later
    LIST_ITEM("list-item", "<aqua><nation_name> <gray>(<nation_leader_name>)"),

    NATION_DISBANDED("nation-disbanded", "<red>Your nation <gray>(<nation_name>)<red> has been disbanded."),

    CANT_QUIT_AS_LEADER("cant-quit-as-leader", "<red>You cannot quit the nation because you are the leader. Use <gray>/nations kick <player><red> to kick all members."),

    QUIT_NATION("quit-nation", "<aqua>You have quit the nation <white><nation_name><aqua>"),

    NO_NATIONS("no-nations", "<gray>There are no nations."),
    YOU_HAVE_KICKED("you-have-kicked", "<red>You have kicked <white><player><aqua`> from your nation."),
    YOU_HAVE_BEEN_KICKED("you-have-been-kicked", "<red>You have been kicked from <white><nation_name><red>"),
    PLAYER_NOT_FOUND("player-not-found", "<red>Player <gray>$0<red> not found."),
    PLAYER_NOT_IN_NATION("player-not-in-nation", "<red>Player <white><player><red> is not in your nation."),
    INVALID_NAME("invalid-name", "<red>Invalid name, only alphanumeric characters are allowed."),
    NEW_NATION("new-nation", "<aqua>You have created the nation <nation_color><nation_name><aqua>"),
    ONLY_PLAYERS("only-players", "<red>Only players can use this command."),

    COMMANDS_HEADER("commands-header", "<aqua>Commands:"),
    COMMANDS_ITEM("commands-item", "<white>- <aqua>/nations $0 $1 <gray>($2)"),
    PLAYER_ALREADY_IN_NATION("player-already-in-nation", "<red>Player <white><player><red> is already in a nation."),
    PLAYER_ALREADY_INVITED("player-already-invited", "<red>Player <white><player><red> is already invited to your nation."),
    INVITED_PLAYER("invited-player", "<aqua>You have invited <white><player><aqua> to your nation."),
    YOU_HAVE_BEEN_INVITED("you-have-been-invited", "<aqua>You have been invited to <white><nation_name><aqua>. Use <gray>/nations join <nation_name><aqua> to join."),
    CHAT_FORMAT("chat-format", "<nation_color><nation_name> <player_name><reset>: <white>");

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

    // the objects may be Players or Nation objects
    public Component replacePlaceholders(Object ...args) {
        String message = this.getDefaultValue();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof Player player) {
                message = message.replace("<player>", player.getName())
                        .replace("<player_uuid>", player.getUniqueId().toString())
                        .replace("<player_displayname>", player.displayName().toString())
                        .replace("<player_name>", player.getName());
            } else if (arg instanceof Nation nation) {
                String leaderName = Bukkit.getOfflinePlayer(nation.leader).getName();
                leaderName = leaderName == null ? "Unknown" : leaderName;
                message = message.replace("<nation_name>", nation.name)
                        .replace("<nation_leader>", nation.leader.toString())
                        .replace("<nation_color>", "<" + nation.color + ">")
                        .replace("<nation_uuid>", nation.uuid.toString())
                        .replace("<nation_leader_name>", leaderName);
            } else if (arg instanceof String str) {
                message = message.replace("$" + i, str);
            }
        }
        return MiniMessage.miniMessage().deserialize(message);
    }
}
