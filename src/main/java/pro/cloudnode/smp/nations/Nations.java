package pro.cloudnode.smp.nations;

import org.bukkit.plugin.java.JavaPlugin;
import pro.cloudnode.smp.nations.commands.NationsCommand;

import java.util.Objects;

public final class Nations extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("nations")).setExecutor(new NationsCommand(this));
        Objects.requireNonNull(this.getCommand("nations")).setTabCompleter(new NationsCommand(this));
    }

    @Override
    public void onDisable() {

    }
}
