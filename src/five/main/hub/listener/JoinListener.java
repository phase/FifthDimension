package five.main.hub.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import five.main.BanSystem;
import five.main.FifthDimension;
import five.main.currency.Currency;
import five.main.hub.util.LobbyManager;
import five.main.hub.util.SkIP;
import five.main.hub.util.SkIP.SkIPData;
import five.main.ranks.RankType;
import five.manager.MessageManager;

public class JoinListener implements Listener {
	
	Currency c = new Currency();
	/**
	 * <b>WARNING! UNSTABLE!</b>
	 */
	@EventHandler
	public void JoinGame(PlayerJoinEvent e){
		final Player p = e.getPlayer();
		p.teleport(FifthDimension.getJoinLocation());
		SkIPData ip = SkIP.getIPData(SkIP.getPlayerIP(p));
		LobbyManager.giveRandomLobby(p);
		e.setJoinMessage("§0《§a5§5d§0》§a "+"§6"+ p.getName() + " joined from " + ip.getRegion() + ", " + ip.getCountryName() + "!");
		p.setFlying(false);
		if(!c.hasCoins(p))
			c.setCoins(p, 0);
		st(new Runnable(){
			public void run() {
				p.teleport(FifthDimension.getSpawnLocation());
				MessageManager.sendMessage(p, "§d§lWelcome to the §a§l5§d§lth Dimension!", true);
				FifthDimension.giveHubScoreboard(p);
				if(!FifthDimension.hasPlayerBefore(p))
					c.giveCoins(p, 10, "Joining the game for the first time!", true);
				else{
					MessageManager.sendMessage(p, "We see you've played here before! Thanks for playing!", true);
				}
				st(new Runnable(){
					public void run(){
						MessageManager.sendMessage(p, "§d§lWalkaround to see the different games.", true);
						st(new Runnable(){
							public void run(){
								MessageManager.sendMessage(p, "§d§lClick the §c§lMagic Clock§d§l to switch lobbies.", true);
								st(new Runnable(){
									public void run(){
										MessageManager.sendMessage(p, "§d§lClick the §6§lCompass§d§l to warp to different games.", true);
									}
								}, 50);
							}
						}, 50);
					}
				}, 50);
			}}, (20*2)+5);
	}
	
	/**
	 * <b>WARNING! UNSTABLE!</b>
	 */
	private int st(Runnable r, long l){
		return Bukkit.getScheduler().scheduleSyncDelayedTask(FifthDimension.getPlugin(), r, l);
	}
	
	
	@EventHandler
	public void loginEvent(PlayerLoginEvent e){
		Player p = e.getPlayer();
		if(BanSystem. isBanned(p.getUniqueId())){
			e.disallow(Result.KICK_BANNED, "§4Banned§0!§r §6Reason: §d" + BanSystem.getReason(p.getUniqueId()));
			return;
		}
		if(e.getResult().equals(Result.KICK_FULL)){
			if(RankType.getRank(p).getPermLevel() > 0){
				e.allow();
			}
		}
	}
	
	
}
