package pro.cloudnode.smp.nations.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.util.BaseCommand;

public class NationsCommand extends BaseCommand {
    public NationsCommand(@NotNull Nations plugin) {
        super(plugin);
    }

    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            //@todo: send help message
            sendMessage("<yellow>Usage: <white>/" + label + " [create|invite|kick|list]");
            return;
        }


    }
}
