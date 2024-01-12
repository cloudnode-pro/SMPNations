package pro.cloudnode.smp.nations.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pro.cloudnode.smp.nations.locale.Messages;
import pro.cloudnode.smp.nations.util.Nation;
import pro.cloudnode.smp.nations.util.NationManager;

import static pro.cloudnode.smp.nations.Nations.t;

public class PlayerConnectionListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NationManager.updatePlayersName(player.getUniqueId());

        Nation n = NationManager.getPlayerNation(player.getUniqueId());
        // set join message
        if (n != null) {
            event.joinMessage(t(Messages.PLAYER_JOIN_SERVER, player, n));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Nation n = NationManager.getPlayerNation(player.getUniqueId());
        // set join message
        if (n != null) {
            event.quitMessage(t(Messages.PLAYER_LEAVE_SERVER, player, n));
        }
    }
}
