package five.main.hub.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import five.main.FifthDimension;
import five.main.hub.util.LobbyManager;

public class LeaveListener implements Listener {
	
	@EventHandler
	public void leaveGame(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(FifthDimension.HubScoreboardIds.containsKey(p.getUniqueId()))
			FifthDimension.HubScoreboardIds.remove(p.getUniqueId());
		LobbyManager.changeLobby(p, -1, false);
	}
	
	
	
}
