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

import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
    private final @NotNull Nations plugin;
    private CommandSender sender;

    public BaseCommand(@NotNull Nations plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the plugin instance
     *
     * @return The plugin instance
     */
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

    /**
     * Check if the sender is a player
     *
     * @return Whether the sender is a player
     */
    public boolean isPlayer() {
        return !(this.sender == null) && this.sender instanceof Player;
    }

    /**
     * Get the sender a player
     *
     * @return The player
     * @implNote You should check with {@link #isPlayer()} before using this method
     */
    public Player getPlayer() {
        return (Player) sender;
    }

    /**
     * Get the command sender
     *
     * @return The command sender
     */

    public CommandSender getSender() {
        return sender;
    }

    /**
     * Send a message to the sender
     *
     * @param message The message to send
     */
    public void sendMessage(String message) {
        this.sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    /**
     * Send a message to the sender
     *
     * @param message The message to send
     */
    public void sendMessage(Component message) {
        this.sender.sendMessage(message);
    }

    /**
     * Send a message to a player
     *
     * @param player  The player to send the message to
     * @param message The message to send
     */
    public void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    /**
     * Send a message to a player
     *
     * @param player  The player to send the message to
     * @param message The message to send
     */
    public void sendMessage(Player player, Component message) {
        player.sendMessage(message);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
