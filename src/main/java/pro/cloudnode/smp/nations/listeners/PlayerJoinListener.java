package pro.cloudnode.smp.nations.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pro.cloudnode.smp.nations.util.NationManager;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NationManager.updatePlayersDisplayname(player.getUniqueId());
    }
}
