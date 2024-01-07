package pro.cloudnode.smp.nations;

import com.loohp.interactivechat.api.InteractiveChatAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pro.cloudnode.smp.nations.commands.NationsCommand;
import pro.cloudnode.smp.nations.listeners.ChatMessageListener;
import pro.cloudnode.smp.nations.locale.Messages;
import pro.cloudnode.smp.nations.util.Nation;
import pro.cloudnode.smp.nations.util.NationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Nations extends JavaPlugin {

    public static NationManager nationManager;

    public static NationManager getNationManager() {
        return nationManager;
    }

    @Override
    public void onEnable() {
        // load nations
        nationManager = new NationManager(this);
        nationManager.load();

        // register commands
        Objects.requireNonNull(this.getCommand("nations")).setExecutor(new NationsCommand(this));
        Objects.requireNonNull(this.getCommand("nations")).setTabCompleter(new NationsCommand(this));

        // register chat listener
        getServer().getPluginManager().registerEvents(new ChatMessageListener(), this);

    }

    @Override
    public void onDisable() {
        nationManager.save();
    }

    public static Component t(Messages message, Object ...args) {
        return MiniMessage.miniMessage().deserialize(message.replacePlaceholders(args));
    }

    public static String ts(Messages message, Object ...args) {
        return message.replacePlaceholders(args);
    }
}
