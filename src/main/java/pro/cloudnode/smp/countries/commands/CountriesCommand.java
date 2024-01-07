package pro.cloudnode.smp.countries.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.countries.Countries;
import pro.cloudnode.smp.countries.util.BaseCommand;

public class CountriesCommand extends BaseCommand {
    public CountriesCommand(@NotNull Countries plugin) {
        super(plugin);
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            //@todo: send help message
            sendMessage("<yellow>Usage: <white>/countries [create|invite|kick|list]");
            return;
        }


    }
}
