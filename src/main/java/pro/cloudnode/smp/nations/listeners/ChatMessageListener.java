package pro.cloudnode.smp.nations.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pro.cloudnode.smp.nations.locale.Messages;
import pro.cloudnode.smp.nations.util.Nation;
import pro.cloudnode.smp.nations.util.NationManager;

import static pro.cloudnode.smp.nations.Nations.t;

public class ChatMessageListener implements Listener {

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Nation nation = NationManager.getPlayerNation(player.getUniqueId());
        Component message;

        if (nation == null) {
            message = t(Messages.CHAT_FORMAT, player, MiniMessage.miniMessage().serialize(event.message()));
        } else {
            message = t(Messages.CHAT_FORMAT_NATION, nation, player, MiniMessage.miniMessage().serialize(event.message()));
        }
        event.setCancelled(true);


        for (Audience recipient : event.viewers()) {
            recipient.sendMessage(message);
        }
    }
}
