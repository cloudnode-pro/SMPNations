package pro.cloudnode.smp.nations;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import pro.cloudnode.smp.nations.commands.NationsCommand;
import pro.cloudnode.smp.nations.listeners.ChatMessageListener;
import pro.cloudnode.smp.nations.locale.Messages;
import pro.cloudnode.smp.nations.util.NationManager;

import java.util.Objects;

public final class Nations extends JavaPlugin {

    public static NationManager nationManager;

    public static NationManager getNationManager() {
        return nationManager;
    }

    /**
     * Translate a message to a component and replace placeholders
     *
     * @param message the message to translate
     * @param args    the arguments to replace placeholders with
     * @return the translated component
     */
    public static Component t(Messages message, Object... args) {
        return MiniMessage.miniMessage().deserialize(message.replacePlaceholders(args));
    }

    /**
     * Translate a message to a string and replace placeholders
     *
     * @param message the message to translate
     * @param args    the arguments to replace placeholders with
     * @return the translated string
     */
    public static String ts(Messages message, Object... args) {
        return message.replacePlaceholders(args);
    }

    @Override
    public void onEnable() {
        // save  messages.yml if it doesn't exist
        Messages.save();
        Messages.addMissingDefaults();
        Messages.load();

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
}
