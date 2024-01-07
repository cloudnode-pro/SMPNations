package pro.cloudnode.smp.nations;

import org.bukkit.plugin.java.JavaPlugin;
import pro.cloudnode.smp.nations.commands.NationsCommand;
import pro.cloudnode.smp.nations.util.NationManager;

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
    }

    @Override
    public void onDisable() {
        nationManager.save();
    }
}
