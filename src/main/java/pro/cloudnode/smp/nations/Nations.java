package pro.cloudnode.smp.nations;

import com.loohp.interactivechat.api.InteractiveChatAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pro.cloudnode.smp.nations.commands.NationsCommand;
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

        // register chat nickname handler
        InteractiveChatAPI.registerNicknameProvider(this, uuid -> {
            List<String> nicknames = new ArrayList<>(); // why we need a list? no idea, but the docs say so

//            StringBuilder builder = new StringBuilder();
//            Player player = getServer().getPlayer(uuid);
//            assert player != null;
//            Nation nation = nationManager.getPlayerNation(uuid);
//
//            if (nation != null) {
//                builder.append(t(Messages.CHAT_FORMAT, nation, player));
//                nicknames.add(builder.toString());
//            } else {
//                nicknames.add("asdsadasda");
//            }

            nicknames.add("asdsadasda");

            return nicknames;
        });
    }

    @Override
    public void onDisable() {
        nationManager.save();
    }

    public static Component t(Messages message, Object ...args) {
        return message.replacePlaceholders(args);
    }
}
