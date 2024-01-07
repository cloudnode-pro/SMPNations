package pro.cloudnode.smp.nations.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.locale.Messages;

import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
    private final @NotNull Nations plugin;
    private CommandSender sender;

    public BaseCommand(@NotNull Nations plugin) {
        this.plugin = plugin;
    }

    public @NotNull Nations getPlugin() {
        return plugin;
    }

    public void execute(CommandSender sender, String label, String[] args) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.sender = sender;
        execute(sender, label, args);
        return true;
    }

    public boolean isPlayer() {
        return !(this.sender == null) && this.sender instanceof Player;
    }

    public Player getPlayer() {
        return (Player) sender;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void sendMessage(String message) {
        this.sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
    public void sendMessage(Component message) {
        this.sender.sendMessage(message);
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public void sendMessage(Player player, Component message) {
        player.sendMessage(message);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
