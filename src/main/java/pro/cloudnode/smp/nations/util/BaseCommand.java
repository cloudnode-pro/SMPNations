package pro.cloudnode.smp.nations.util;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
    private @Nullable CommandSender sender;

    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.sender = sender;
        return execute(sender, label, args);
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
     * You should check with {@link #isPlayer()} before using this method
     */
    public Player getPlayer() {
        return (Player) sender;
    }

    /**
     * Send a message to the sender
     *
     * @param message The message to send
     */
    public boolean sendMessage(@NotNull Component message) {
        if (this.sender != null) {
            this.sender.sendMessage(message);
        }
        return true;
    }

    /**
     * Send a message to a player
     *
     * @param player  The player to send the message to
     * @param message The message to send
     */
    public boolean sendMessage(@NotNull Player player, @NotNull Component message) {
        player.sendMessage(message);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
