package pro.cloudnode.smp.nations.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pro.cloudnode.smp.nations.Nations;
import pro.cloudnode.smp.nations.locale.Messages;
import pro.cloudnode.smp.nations.util.Nation;

import static pro.cloudnode.smp.nations.Nations.t;

public class ChatMessageListener implements Listener {

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Nation nation = Nations.getNationManager().getPlayerNation(player.getUniqueId());
        if (nation != null) {
//            event.message(t(Messages.CHAT_FORMAT, nation, player, event.message()));
            Bukkit.broadcast(t(Messages.CHAT_FORMAT, nation, player, MiniMessage.miniMessage().serialize(event.message())));
            event.setCancelled(true);
        }
    }
}
