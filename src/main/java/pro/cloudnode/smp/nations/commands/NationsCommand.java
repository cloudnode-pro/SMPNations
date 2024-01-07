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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class NationsCommand extends BaseCommand {
    public NationsCommand(@NotNull Nations plugin) {
        super(plugin);
    }

    public void execute(CommandSender sender, String label, String[] args) {
        if (!isPlayer()) {
            sendMessage(t(Messages.ONLY_PLAYERS));
            return;
        }

        if (args.length == 0) {
            help(sender, label, args);
            return;
        }

        switch (args[0]) {
            case "create":
                newNation(sender, label, args);
                break;
            case "invite":
                invite(sender, label, args);
                break;
            case "kick":
                kick(sender, label, args);
                break;
            case "list":
                list(sender, label, args);
                break;
            case "quit":
                quit(sender, label, args);
                break;
            case "info":
                info(sender, label, args);
                break;
            case "option":
                option(sender, label, args);
                break;
            case "join":
                join(sender, label, args);
                break;
            case "help":
            default:
                help(sender, label, args);
                break;
        }
    }

    // help for each command
    private void help(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            // list of commands
            sendMessage(t(Messages.COMMANDS_HEADER));
            sendMessage(t(Messages.COMMANDS_ITEM, "create", "<name>", "create a new nation"));
            sendMessage(t(Messages.COMMANDS_ITEM, "invite", "<player>", "invite a player to your nation"));
            sendMessage(t(Messages.COMMANDS_ITEM, "kick", "<player>", "kick a player from your nation"));
            sendMessage(t(Messages.COMMANDS_ITEM, "list", "", "list all nations"));
            sendMessage(t(Messages.COMMANDS_ITEM, "quit", "", "quit your nation"));
            sendMessage(t(Messages.COMMANDS_ITEM, "info", "", "get info about your nation"));
            sendMessage(t(Messages.COMMANDS_ITEM, "option", "<key> <value>", "set options for your nation"));
            sendMessage(t(Messages.COMMANDS_ITEM, "join", "<nation>", "join a nation"));
            sendMessage(t(Messages.COMMANDS_ITEM, "help", "", "show this help message"));
            return;
        }

        switch (args[1]) {
            case "create":
                sendMessage(t(Messages.USAGE, label, "create", "<name>"));
                break;
            case "invite":
                sendMessage(t(Messages.USAGE, label, "invite", "<player>"));
                break;
            case "kick":
                sendMessage(t(Messages.USAGE, label, "kick", "<player>"));
                break;
            case "list":
                sendMessage(t(Messages.USAGE, label, "list", ""));
                break;
            case "quit":
                sendMessage(t(Messages.USAGE, label, "quit", ""));
                break;
            case "info":
                sendMessage(t(Messages.USAGE, label, "info", ""));
                break;
            case "option":
                sendMessage(t(Messages.USAGE, label, "option", "<key> <value>"));
                break;
            case "join":
                sendMessage(t(Messages.USAGE, label, "join", "<nation>"));
                break;
            default:
                help(sender, label, new String[0]);
                break;
        }
    }

    private void join(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage(t(Messages.USAGE, label, "join", "<nation>"));
            return;
        }

        Nation nation = Nations.getNationManager().get(args[1]);
        if (nation == null) {
            sendMessage(t(Messages.NATION_NOT_FOUND, args[1]));
            return;
        }

        if (!nation.invited.contains(getPlayer().getUniqueId())) {
            sendMessage(t(Messages.NOT_INVITED_TO_NATION, nation));
            return;
        }

        nation.addMember(getPlayer().getUniqueId());
        nation.removeInvited(getPlayer().getUniqueId());
        sendMessage(t(Messages.JOINED_NATION, nation));
    }

    private void option(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage(t(Messages.USAGE, label, "option", "<key> <value>"));
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage(t(Messages.NOT_IN_NATION));
            return;
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage(t(Messages.NOT_LEADER));
            return;
        }

        String key = args[1];

        switch (key) {
            case "color":
                if (args.length == 2) {
                    sendMessage(t(Messages.USAGE, label, "option", "color <color>"));
                    return;
                }
                String color = args[2];
                // #hex or color
                if (color.matches("^(?:#(?:[0-9a-fA-F]{3}){1,2}\\b|\\b\\w+\\b)")) {
                    // check if its hex
                    if (!color.startsWith("#") && color.length() == 6 && color.matches("[0-9a-fA-F]+")) {
                        color = "#" + color;
                    }
                    nation.color = color;
                    sendMessage(t(Messages.COLOR_SET, color));
                } else {
                    sendMessage(t(Messages.INVALID_COLOR));
                }

                break;
            default:
                sendMessage(t(Messages.USAGE, label, "option", "<key> <value>"));
                break;
        }

    }

    private void info(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            sendMessage(t(Messages.USAGE, label, "info", ""));
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage(t(Messages.NOT_IN_NATION));
            return;
        }

        sendMessage(t(Messages.INFO_HEADER, nation));
        sendMessage(t(Messages.INFO_LEADER, Bukkit.getPlayer(nation.leader).getName()));
        sendMessage(t(Messages.INFO_MEMBERS, nation.members.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).map(Player::getName).toList().toString()));
    }

    private void quit(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            sendMessage(t(Messages.USAGE, label, "quit", ""));
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage(t(Messages.NOT_IN_NATION));
            return;
        }

        if (nation.members.isEmpty() && nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage(t(Messages.NATION_DISBANDED, nation));
            Nations.getNationManager().remove(nation);
            return;
        }

        if (nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage(t(Messages.CANT_QUIT_AS_LEADER));
            return;
        }

        sendMessage(t(Messages.QUIT_NATION, nation));
        nation.removeMember(getPlayer().getUniqueId());
    }

    private void list(CommandSender sender, String label, String[] args) {
        sendMessage(t(Messages.LIST_HEADER));
        if (Nations.getNationManager().nations.isEmpty()) {
            sendMessage(t(Messages.NO_NATIONS));
            return;
        }
        for (Nation nation : Nations.getNationManager().nations.values()) {
            sendMessage(t(Messages.LIST_ITEM, nation));
        }
    }

    private void kick(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage(t(Messages.USAGE, label, "kick", "<player>"));
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage(t(Messages.NOT_IN_NATION));
            return;
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage(t(Messages.NOT_LEADER));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sendMessage(t(Messages.PLAYER_NOT_FOUND, args[1]));
            return;
        }

        if (!nation.members.contains(player.getUniqueId())) {
            sendMessage(t(Messages.PLAYER_NOT_IN_NATION, player));
            return;
        }

        nation.removeMember(player.getUniqueId());
        sendMessage(t(Messages.YOU_HAVE_KICKED, player));
        sendMessage(player, t(Messages.YOU_HAVE_BEEN_KICKED, nation));
    }

    private void invite(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage(t(Messages.USAGE, label, "invite", "<player>"));
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage(t(Messages.NOT_IN_NATION));
            return;
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage(t(Messages.NOT_LEADER));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sendMessage(t(Messages.PLAYER_NOT_FOUND, args[1]));
            return;
        }
        Nation inviteeNation = Nations.getNationManager().getPlayerNation(player.getUniqueId());

        if (inviteeNation != null) {
            sendMessage(t(Messages.PLAYER_ALREADY_IN_NATION, player));
            return;
        }

        if (nation.invited.contains(player.getUniqueId())) {
            sendMessage(t(Messages.PLAYER_ALREADY_INVITED, player));
            return;
        }

        nation.addInvited(player.getUniqueId());
        sendMessage(t(Messages.INVITED_PLAYER, player));
        sendMessage(player, t(Messages.YOU_HAVE_BEEN_INVITED, nation));
    }

    public void newNation(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage(t(Messages.USAGE, label, "create", "<name>"));
            return;
        }

        String name = args[1];

        // check if name is valid (ascii)
        if (!name.matches("^[a-zA-Z0-9]*$")) {
            sendMessage(t(Messages.INVALID_NAME));
            return;
        }

        // create nation
        Nation nation = new Nation(name, getPlayer().getUniqueId());

        // save nation
        Nations.getNationManager().add(nation);

        // send message
        sendMessage(t(Messages.NEW_NATION, nation));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Stream<String> commands = Stream.of("create", "invite", "kick", "list", "quit", "info", "option", "join", "help");
        return switch (args.length) {
            case 1 ->
                    commands.filter(s -> s.startsWith(args[0])).toList();
            case 2 -> switch (args[0]) {
                case "create" -> List.of("<name>");
                case "invite", "kick" -> List.of("<player>");
                case "option" -> List.of("color");
                case "join" -> List.of("<nation>");
                case "help" -> commands.filter(s -> s.startsWith(args[1])).toList();
                default -> null;
            };
            case 3 -> switch (args[1]) {
                case "color" -> List.of("<color>");
                default -> null;
            };
            default -> null;
        };
    }
}
