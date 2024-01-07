package pro.cloudnode.smp.countries;

import org.bukkit.plugin.java.JavaPlugin;
import pro.cloudnode.smp.countries.commands.CountriesCommand;

import java.util.Objects;

public final class Countries extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("countries")).setExecutor(new CountriesCommand(this));
        Objects.requireNonNull(this.getCommand("countries")).setTabCompleter(new CountriesCommand(this));
    }

    @Override
    public void onDisable() {

    }
}
