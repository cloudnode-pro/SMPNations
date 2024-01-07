package pro.cloudnode.smp.nations.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.util.BaseCommand;
import pro.cloudnode.smp.nations.util.Nation;

import java.util.List;
import java.util.stream.Stream;

public class NationsCommand extends BaseCommand {
    public NationsCommand(@NotNull Nations plugin) {
        super(plugin);
    }

    public void execute(CommandSender sender, String label, String[] args) {
        if (!isPlayer()) {
            sendMessage("<red>Only players can use this command.");
            return;
        }

        if (args.length == 0) {
            //@todo: send help message
            sendMessage("<yellow>Usage: <white>/" + label + " [create|invite|kick|list|quit|accept]");
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
            default:
                sendMessage("<yellow>Usage: <white>/" + label + " [create|invite|kick|list|quit|join]");
                break;
        }
    }

    private void join(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage("<yellow>Usage: <white>/" + label + " join <nation>");
            return;
        }

        Nation nation = Nations.getNationManager().get(args[1]);
        if (nation == null) {
            sendMessage("<red>Nation <white>" + args[1] + "<red> not found.");
            return;
        }

        if (!nation.invited.contains(getPlayer().getUniqueId())) {
            sendMessage("<red>You have not been invited to the nation <white>" + nation.name + "<red>.");
            return;
        }

        nation.addMember(getPlayer().getUniqueId());
        nation.removeInvited(getPlayer().getUniqueId());
        sendMessage("<green>You have joined the nation <white>" + nation.name + "<green>.");
    }

    private void option(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage("<yellow>Usage: <white>/" + label + " option <key> <value>");
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage("<red>You are not in a nation.");
            return;
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage("<red>You are not the leader of your nation.");
            return;
        }

        String key = args[1];

        switch (key) {
            case "color":
                if (args.length == 2) {
                    sendMessage("<yellow>Usage: <white>/" + label + " option color <color>");
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
                    sendMessage("<green>You have set the color of your nation to <" + color +">" + color + "<green>.");
                } else {
                    sendMessage("<red>Invalid color, must be a hex code or a color name.");
                }

                break;
            default:
                sendMessage("<yellow>Usage: <white>/" + label + " option <key> <value>");
                break;
        }

    }

    private void info(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            sendMessage("<yellow>Usage: <white>/" + label + " info");
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage("<red>You are not in a nation.");
            return;
        }

        sendMessage("<yellow>Info for nation <" + nation.color + ">" + nation.name + "<yellow>:");
        sendMessage("<white>- <yellow>Leader: <white>" + Bukkit.getPlayer(nation.leader).getName());
        StringBuilder members = new StringBuilder();
        // add members (green if online, red if offline)
        nation.members.forEach(member -> {
            if (Bukkit.getPlayer(member) != null) {
                members.append("<green>").append(Bukkit.getPlayer(member).getName());
            } else {
                members.append("<red>").append(Bukkit.getOfflinePlayer(member).getName());
            }
            members.append("<white>, ");
        });
        sendMessage("<white>- <yellow>Members: <white>" + members);
        sendMessage("<white>- <yellow>Color: <white>" + nation.color);
    }

    private void quit(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            sendMessage("<yellow>Usage: <white>/" + label + " quit");
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage("<red>You are not in a nation.");
            return;
        }

        if (nation.members.isEmpty() && nation.leader.equals(getPlayer().getUniqueId())) {
            Nations.getNationManager().remove(nation);
            sendMessage("<green>You have disbanded the nation <white>" + nation.name + "<green>.");
        }

        if (nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage("<red>You cannot quit the nation because you are the leader. Use <white>/nations kick <player><red> to kick a member.");
            return;
        }

        sendMessage("<green>You have quit the nation <white>" + nation.name + "<green>.");
        nation.removeMember(getPlayer().getUniqueId());
    }

    private void list(CommandSender sender, String label, String[] args) {
        sendMessage("<yellow>Listing all nations:");
        for (Nation nation : Nations.getNationManager().nations.values()) {
            sendMessage("<white>- <yellow><hover:show_text:'<white>Leader: <yellow>" + Bukkit.getPlayer(nation.leader).getName() + "\n<white>Members: <yellow>" + nation.members.size() + "'>" + nation.name);
        }
    }

    private void kick(CommandSender sender, String label, String[] args) {

    }

    private void invite(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage("<yellow>Usage: <white>/" + label + " invite <player>");
            return;
        }

        Nation nation = Nations.getNationManager().getPlayerNation(getPlayer().getUniqueId());
        if (nation == null) {
            sendMessage("<red>You are not in a nation.");
            return;
        }

        if (!nation.leader.equals(getPlayer().getUniqueId())) {
            sendMessage("<red>You are not the leader of your nation.");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sendMessage("<red>Player <white>" + args[1] + "<red> not found.");
            return;
        }

        if (nation.members.contains(player.getUniqueId())) {
            sendMessage("<red>Player <white>" + player.getName() + "<red> is already in your nation.");
            return;
        }

        if (nation.invited.contains(player.getUniqueId())) {
            sendMessage("<red>Player <white>" + player.getName() + "<red> is already invited to your nation.");
            return;
        }

        nation.addInvited(player.getUniqueId());
        sendMessage("<green>You have invited <white>" + player.getName() + "<green> to your nation.");
        sendMessage(player, "<green>You have been invited to the nation <white>" + nation.name + "<green>. Use <white>/nations join " + nation.name + "<green> to join.");
    }

    public void newNation(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            sendMessage("<yellow>Usage: <white>/" + label + " create <name>");
            return;
        }

        String name = args[1];

        // check if name is valid (ascii)
        if (!name.matches("^[a-zA-Z0-9]*$")) {
            sendMessage("<red>Invalid name, only alphanumeric characters are allowed.");
            return;
        }

        // create nation
        Nation nation = new Nation(name, getPlayer().getUniqueId());

        // save nation
        Nations.getNationManager().add(nation);

        // send message
        sendMessage("<green>You have created a new nation called <white>" + name + "<green>.");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (args.length) {
            case 1 ->
                    Stream.of("create", "invite", "kick", "list", "quit", "info", "option", "accept").filter(s -> s.startsWith(args[0])).toList();
            case 2 -> switch (args[0]) {
                case "create" -> List.of("<name>");
                case "invite", "kick" -> List.of("<player>");
                case "option" -> List.of("color");
                case "join" -> List.of("<nation>");
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
