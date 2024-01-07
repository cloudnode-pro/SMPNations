package pro.cloudnode.smp.countries.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.countries.Countries;

import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
    private final @NotNull Countries plugin;
    private CommandSender sender;

    public BaseCommand(@NotNull Countries plugin) {
        this.plugin = plugin;
    }

    public @NotNull Countries getPlugin() {
        return plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.sender = sender;
        execute(sender, args);
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
