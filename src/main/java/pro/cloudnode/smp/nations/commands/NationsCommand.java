package pro.cloudnode.smp.nations.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.locale.Messages;
import pro.cloudnode.smp.nations.util.BaseCommand;
import pro.cloudnode.smp.nations.util.Nation;
import pro.cloudnode.smp.nations.util.NationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pro.cloudnode.smp.nations.Nations.t;

public class NationsCommand extends BaseCommand {
    private final @NotNull List<String> COLORS = List.of("white", "red", "blue", "green", "yellow", "light_purple", "aqua", "pink", "gray", "dark_gray", "dark_red", "dark_blue", "dark_green", "dark_aqua", "dark_purple");
    private final @NotNull List<String> EMPTY = new ArrayList<>();

    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!isPlayer()) {
            return sendMessage(t(Messages.ONLY_PLAYERS));
        }

        if (args.length == 0) {
            help(sender, label, args);
            return false;
        }

        return switch (args[0]) {
            case "create" -> newNation(sender, label, args);
            case "invite" -> invite(sender, label, args);
            case "kick" -> kick(sender, label, args);
            case "list" -> list(sender, label, args);
            case "quit", "leave" -> quit(sender, label, args);
            case "info" -> info(sender, label, args);
            case "option" -> option(sender, label, args);
            case "join" -> join(sender, label, args);
            case "reload" -> reload(sender, label, args);
            case "force-delete" -> forceDelete(sender, label, args);
            case "cancel-invite" -> cancelInvite(sender, label, args);
            default -> help(sender, label, args);
        };
    }

    private boolean cancelInvite(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return sendMessage(t(Messages.USAGE, label, "cancel-invite", "<player>"));
        }

        Nation nation = NationManager.getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            return sendMessage(t(Messages.NOT_IN_NATION));
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            return sendMessage(t(Messages.NOT_LEADER));
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            return sendMessage(t(Messages.PLAYER_NOT_FOUND, args[1]));
        }

        if (!nation.invited.contains(player.getUniqueId())) {
            return sendMessage(t(Messages.PLAYER_NOT_INVITED, player));
        }

        nation.removeInvited(player.getUniqueId());
        return sendMessage(t(Messages.CANCELED_INVITE, player));
    }

    // help for each command
    private boolean help(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // send plugin info
        if (args.length == 0) {
            sendMessage(t(Messages.PLUGIN_INFO, Nations.getVersion(), Nations.getMinecraftVersion()));
            return true;
        }

        switch (args[1]) {
            case "create" -> sendMessage(t(Messages.USAGE, label, "create", "<name>"));
            case "invite" -> sendMessage(t(Messages.USAGE, label, "invite", "<player>"));
            case "kick" -> sendMessage(t(Messages.USAGE, label, "kick", "<player>"));
            case "list" -> sendMessage(t(Messages.USAGE, label, "list", ""));
            case "quit", "leave" -> sendMessage(t(Messages.USAGE, label, "quit", ""));
            case "info" -> sendMessage(t(Messages.USAGE, label, "info", ""));
            case "option" -> sendMessage(t(Messages.USAGE, label, "option", "<key> <value>"));
            case "join" -> sendMessage(t(Messages.USAGE, label, "join", "<nation>"));
            case "cancel-invite" -> sendMessage(t(Messages.USAGE, label, "cancel-invite", "<player>"));
            default -> help(sender, label, new String[0]);
        }

        return true;
    }

    private boolean join(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return sendMessage(t(Messages.USAGE, label, "join", "<nation>"));
        }

        if (NationManager.isInNation((Player) sender)) {
            return sendMessage(t(Messages.ALREADY_IN_NATION));
        }

        Nation nation = Nations.getNationManager().get(args[1]);
        if (nation == null) {
            return sendMessage(t(Messages.NATION_NOT_FOUND, args[1]));
        }

        if (!nation.invited.contains(getPlayer().getUniqueId())) {
            return sendMessage(t(Messages.NOT_INVITED_TO_NATION, nation));
        }

        nation.addMember(getPlayer().getUniqueId());
        nation.removeInvited(getPlayer().getUniqueId());
        return sendMessage(t(Messages.JOINED_NATION, nation));
    }

    private boolean option(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return sendMessage(t(Messages.USAGE, label, "option", "<key> <value>"));
        }

        Nation nation = NationManager.getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            return sendMessage(t(Messages.NOT_IN_NATION));
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            return sendMessage(t(Messages.NOT_LEADER));
        }

        String key = args[1];

        if (key.equals("color")) {
            if (args.length == 2) {
                return sendMessage(t(Messages.USAGE, label, "option", "color <color>"));
            }
            String color = args[2].toLowerCase();
            boolean isHex = color.matches("^#[0-9a-f]{6}$");
            if (!COLORS.contains(color) && !isHex) {
                return sendMessage(t(Messages.INVALID_COLOR));
            }

            nation.color = color;
            sendMessage(t(Messages.COLOR_SET, color));
            nation.update();
        } else {
            sendMessage(t(Messages.USAGE, label, "option", "<key> <value>"));
        }

        return true;
    }

    private boolean info(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            return sendMessage(t(Messages.USAGE, label, "info", ""));
        }

        Nation nation = NationManager.getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            return sendMessage(t(Messages.NOT_IN_NATION));
        }

        sendMessage(t(Messages.INFO_HEADER, nation));
        sendMessage(t(Messages.INFO_LEADER, Objects.requireNonNull(Bukkit.getPlayer(nation.leader)).getName()));
        return sendMessage(t(Messages.INFO_MEMBERS, nation.members.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).map(Player::getName).toList().toString()));
    }

    private boolean quit(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            return sendMessage(t(Messages.USAGE, label, "quit", ""));
        }

        Nation nation = NationManager.getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            return sendMessage(t(Messages.NOT_IN_NATION));
        }

        if (nation.members.isEmpty() && nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage(t(Messages.NATION_DISBANDED, nation));
            Nations.getNationManager().remove(nation);
            return true;
        }

        if (nation.leader.equals(getPlayer().getUniqueId())) {
            return sendMessage(t(Messages.CANT_QUIT_AS_LEADER));
        }

        sendMessage(t(Messages.QUIT_NATION, nation));
        nation.removeMember(getPlayer().getUniqueId());

        return true;
    }

    private boolean list(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        sendMessage(t(Messages.LIST_HEADER));
        if (NationManager.nations.isEmpty()) {
            return sendMessage(t(Messages.NO_NATIONS));
        }
        for (Nation nation : NationManager.nations.values()) {
            sendMessage(t(Messages.LIST_ITEM, nation));
        }
        return true;
    }

    private boolean forceDelete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("nations.admin") && !sender.hasPermission("nations.forceDelete")) {
            return sendMessage(t(Messages.NO_PERMISSION));
        }

        if (args.length <= 1) {
            return sendMessage(t(Messages.USAGE, label, "force-delete", "<nation>"));
        }

        Nation nation = Nations.getNationManager().get(args[1]);
        if (nation == null) {
            return sendMessage(t(Messages.NATION_NOT_FOUND, args[1]));
        }

        Nations.getNationManager().remove(nation);
        return sendMessage(t(Messages.NATION_DELETED, nation));
    }

    private boolean reload(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("nations.admin") && !sender.hasPermission("nations.reload")) {
            return sendMessage(t(Messages.NO_PERMISSION));
        }
        if (args.length > 1) {
            return sendMessage(t(Messages.USAGE, label, "reload", ""));
        }

        Nations.getPlugin(Nations.class).reloadConfig();
        Messages.load();
        return sendMessage(t(Messages.RELOADED));
    }

    private boolean kick(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return sendMessage(t(Messages.USAGE, label, "kick", "<player>"));
        }

        Nation nation = NationManager.getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            return sendMessage(t(Messages.NOT_IN_NATION));
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            return sendMessage(t(Messages.NOT_LEADER));
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            return sendMessage(t(Messages.PLAYER_NOT_FOUND, args[1]));
        }

        if (!nation.members.contains(player.getUniqueId())) {
            return sendMessage(t(Messages.PLAYER_NOT_IN_NATION, player));
        }

        nation.removeMember(player.getUniqueId());
        sendMessage(t(Messages.YOU_HAVE_KICKED, player));
        return sendMessage(player, t(Messages.YOU_HAVE_BEEN_KICKED, nation));
    }

    private boolean invite(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return sendMessage(t(Messages.USAGE, label, "invite", "<player>"));
        }

        Nation nation = NationManager.getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            return sendMessage(t(Messages.NOT_IN_NATION));
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            return sendMessage(t(Messages.NOT_LEADER));
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            return sendMessage(t(Messages.PLAYER_NOT_FOUND, args[1]));
        }
        Nation inviteeNation = NationManager.getPlayerNation(player.getUniqueId());

        if (inviteeNation != null) {
            return sendMessage(t(Messages.PLAYER_ALREADY_IN_NATION, player));
        }

        if (nation.invited.contains(player.getUniqueId())) {
            return sendMessage(t(Messages.PLAYER_ALREADY_INVITED, player));
        }

        nation.addInvited(player.getUniqueId());
        sendMessage(t(Messages.INVITED_PLAYER, player));
        return sendMessage(player, t(Messages.YOU_HAVE_BEEN_INVITED, nation));
    }

    public boolean newNation(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return sendMessage(t(Messages.USAGE, label, "create", "<name>"));
        }

        if (NationManager.isInNation((Player) sender)) {
            return sendMessage(t(Messages.ALREADY_IN_NATION));
        }

        String name = args[1];

        // check if name is valid (ascii)
        if (!name.matches("^[a-zA-Z0-9]*$") || name.length() > 16) {
            return sendMessage(t(Messages.INVALID_NAME));
        }

        // create nation
        Nation nation = new Nation(name, getPlayer().getUniqueId());

        // save nation
        Nations.getNationManager().add(nation);

        // send message
        return sendMessage(t(Messages.NEW_NATION, nation));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Stream<String> commands = Stream.of("list", "help");
        Nation nation = null;
        if (NationManager.isNationLeader(player.getUniqueId()))
            commands = Stream.concat(commands, Stream.of("kick", "invite", "cancel-invite", "option"));
        if (NationManager.isInNation(player.getUniqueId())) {
            commands = Stream.concat(commands, Stream.of("quit", "info"));
            nation = NationManager.getPlayerNation(player.getUniqueId());
        } else commands = Stream.concat(commands, Stream.of("join", "create"));
        if (sender.hasPermission("nations.admin"))
            commands = Stream.concat(commands, Stream.of("force-delete", "reload"));


        // sort alphabetically
        commands = commands.sorted();
        return switch (args.length) {
            case 1 -> commands.filter(s -> s.startsWith(args[0])).toList();
            case 2 -> switch (args[0]) {
                case "create" -> List.of("<name>");
                case "invite", "kick" -> null;
                case "option" -> List.of("color");
                case "join" -> Nations.getNationManager().getNationsInviting(player.getUniqueId());
                case "force-delete" -> Nations.getNationManager().getNations();
                case "cancel-invite" -> {
                    if (nation == null) yield EMPTY;
                    yield NationManager.getNationInvites(nation).stream().map(Player::getName).toList();
                }
                case "help" -> commands.filter(s -> s.startsWith(args[1])).toList();
                default -> EMPTY;
            };
            case 3 -> switch (args[1]) {
                case "color" -> COLORS.stream().filter(s -> s.startsWith(args[2])).toList();
                default -> EMPTY;
            };
            default -> EMPTY;
        };
    }
}
