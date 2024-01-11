package pro.cloudnode.smp.nations.locale;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.util.Nation;

import java.io.File;

public enum Messages {
    USAGE("usage", "<aqua>Usage: <white>/$0 $1 <gray>$2"),
    NO_PERMISSION("no-permission", "<red>(!) You do not have permission to use this command."),

    NATION_NOT_FOUND("nation-not-found", "<red>(!) Nation <white>$0<red> not found."),

    NOT_INVITED_TO_NATION("not-invited-to-nation", "<red>(!) You are not invited to <white><nation_name>"),
    JOINED_NATION("joined-nation", "<aqua>You have joined the nation <white><nation_name><aqua>"),
    NOT_IN_NATION("not-in-nation", "<red>(!) You are not in a nation."),
    NOT_LEADER("not-leader", "<red>(!) You are not the leader of your nation."),
    COLOR_SET("color-set", "<aqua>You have set your nation color to <$0>$0<aqua>"),
    INVALID_COLOR("invalid-color", "<red>(!) Invalid color, must be a hex code <gray>(<white>#<red>RR<green>GG<blue>BB<gray>)<red> or a color name."),

    INFO_HEADER("info-header", "<aqua>Info for <nation_color><nation_name><aqua>"),
    INFO_LEADER("info-leader", "<aqua>Leader: <white>$0"),
    INFO_MEMBERS("info-members", "<aqua>Members: <white>$0"),
    INFO_COLOR("info-color", "<aqua>Color: <white><color>"),

    LIST_HEADER("list-header", "<aqua>Nations:"),

    LIST_ITEM("list-item", "<gray>– <reset><hover:show_text:'<nation_color><bold><nation_name><reset><newline><aqua>Leader: <white><nation_leader_name><newline><aqua>Members: <white><nation_members>'><nation_color><nation_name><reset>"),

    NATION_DISBANDED("nation-disbanded", "<red>(!) Your nation <gray>(<nation_name>)<red> has been disbanded."),

    CANT_QUIT_AS_LEADER("cant-quit-as-leader", "<red>(!) You cannot quit the nation because you are the leader. Use <gray>/nations kick <player><red> to kick all members."),

    QUIT_NATION("quit-nation", "<aqua>You have quit the nation <white><nation_name><aqua>"),

    NO_NATIONS("no-nations", "<gray>There are no nations."),
    YOU_HAVE_KICKED("you-have-kicked", "<red>(!) You have kicked <white><player><aqua`> from your nation."),
    YOU_HAVE_BEEN_KICKED("you-have-been-kicked", "<red>(!) You have been kicked from <white><nation_name>"),
    PLAYER_NOT_FOUND("player-not-found", "<red>(!) Player <gray>$0<red> Not found."),
    PLAYER_NOT_IN_NATION("player-not-in-nation", "<red>(!) Player <white><player><red> is not in your nation."),
    INVALID_NAME("invalid-name", "<red>(!) Invalid name, only alphanumeric characters are allowed and may not be longer than 16 characters."),
    NEW_NATION("new-nation", "<aqua>You have created the nation <nation_color><nation_name><aqua>"),
    ONLY_PLAYERS("only-players", "<red>(!) Only players can use this command."),

    COMMANDS_HEADER("commands-header", "<aqua>Commands:"),
    COMMANDS_ITEM("commands-item", "<white>- <aqua>/nations $0 $1 <gray>($2)"),
    PLAYER_ALREADY_IN_NATION("player-already-in-nation", "<red>(!) Player <white><player><red> is already in a nation."),
    PLAYER_ALREADY_INVITED("player-already-invited", "<red>(!) Player <white><player><red> is already invited to your nation."),
    INVITED_PLAYER("invited-player", "<aqua>You have invited <white><player><aqua> to your nation."),
    YOU_HAVE_BEEN_INVITED("you-have-been-invited", "<aqua>You have been invited to <white><nation_name><aqua>. Use <gray>/nations join <nation_name><aqua> to join."),
    CHAT_FORMAT_NATION("chat-format-nation", "<gray>(<nation_color><nation_name><gray>) <nation_color><player_name> <dark_gray>» <white>$2"),
    CHAT_FORMAT("chat-format", "<player_name> <dark_gray>» <white>$1"),
    NATION_DELETED("nation-deleted", "<aqua>You have deleted the nation <white><nation_name><aqua>"),
    RELOADED("reloaded", "<aqua>Nations has been reloaded."),
    ALREADY_IN_NATION("already-in-nation", "<red>(!) You are already in a nation."),
    NATION_DISPLAYNAME("nation-displayname", "<gray>(<nation_color><nation_name><gray>) <nation_color><player_name>"),
    PLAYER_DISPLAYNAME("player-displayname", "<gray><player_name>"),
    PLAYER_JOIN_SERVER("player-join-server", "<gray>[<green>+<gray>] <gray>(<nation_color><nation_name><gray>) <nation_color><player_name>"),
    PLAYER_LEAVE_SERVER("player-leave-server", "<gray>[<red>-<gray>] <gray>(<nation_color><nation_name><gray>) <nation_color><player_name>");

    public final String key;

    public String default_value;

    Messages(String key, String default_value) {
        this.key = key;
        this.default_value = default_value;
    }

    /**
     * Save messages to the config
     *
     * This will *not* overwrite the config
     * This saves into `plugins/Nations/messages.yml`
     */
    public static void save() {
        // check if messages.yml exists
        File messagesFile = new File("plugins/Nations/messages.yml");
        if (messagesFile.exists()) return;

        YamlConfiguration config = new YamlConfiguration();

        for (Messages message : Messages.values()) {
            config.set("messages." + message.getKey(), message.getDefaultValue());
        }

        try {
            config.save(new File("plugins/Nations/messages.yml"));
            Nations.getPlugin(Nations.class).getLogger().info("Saved default messages.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load messages from the config
     *
     * This loads from `plugins/Nations/messages.yml`
     */
    public static void load() {
        // add missing first
        addMissingDefaults();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/Nations/messages.yml"));

        for (Messages message : Messages.values()) {
            message.default_value = config.getString("messages." + message.getKey());
        }
    }

    /**
     * Adds the default messages to the config if they don't exist
     *
     * This saves into `plugins/Nations/messages.yml`
     */
    public static void addMissingDefaults() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/Nations/messages.yml"));

        for (Messages message : Messages.values()) {
            if (!config.contains("messages." + message.getKey())) {
                config.set("messages." + message.getKey(), message.getDefaultValue());
            }
        }

        try {
            config.save(new File("plugins/Nations/messages.yml"));
            Nations.getPlugin(Nations.class).getLogger().info("Added missing messages to messages.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return default_value;
    }

    /**
     * Translate a message to a component and replace placeholders
     *
     * @param args the arguments to replace placeholders with
     * @return the translated component
     */
    public String replacePlaceholders(Object... args) {
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
                        .replace("<nation_leader_name>", leaderName)
                        .replace("<nation_members>", nation.members.size() + 1 + "");
            } else if (arg instanceof String str) {
                message = message.replace("$" + i, str);
            }
        }
        return message;
    }
}
