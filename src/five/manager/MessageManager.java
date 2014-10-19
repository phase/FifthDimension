package five.manager;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

import five.main.hub.util.LobbyManager;
import five.main.ranks.Rank;
import five.main.ranks.RankType;

public class MessageManager {

	private static String prefix = "§0《§a5§5d§0》§a ";
	private final static String chatPrefix = "§5";
	private final static String chatSuffix = " ⇛§7  ";
	
	public static String sendMessage(Player p, String s, boolean playNote){
		p.sendMessage(prefix+s);
		if(playNote)
			p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A));
		return prefix+s;
	}

	public static void chatMessage(Player p, String m) {
		String chatPrefix2 = chatPrefix;
		if(Rank.getVips().contains(p.getUniqueId()))
			chatPrefix2 = "§0(§5§lVIP§0)§r " + chatPrefix2;
		if(Rank.getAdmins().contains(p.getUniqueId()))
			chatPrefix2 = "§0(§c§lADMIN§0)§r " + chatPrefix2;
		if(Rank.getOwners().contains(p.getUniqueId()))
			chatPrefix2 = "§0(§a§lOWNER§0)§r " + chatPrefix2;
		if(Rank.getDevelopers().contains(p.getUniqueId()))
			chatPrefix2 = "§0(§6§lDEV§0)§r " + chatPrefix2;
		if(RankType.getRank(p).getPermLevel() > 0){
			Bukkit.broadcastMessage(chatPrefix2 + p.getName() + chatSuffix + m);
		}else{
			for(Player o : Bukkit.getOnlinePlayers()){
				if(LobbyManager.getLobby(p) == LobbyManager.getLobby(o)){
					o.sendMessage(chatPrefix2 + p.getName() + chatSuffix + m);
				}
			}
		}
		
	}

	public static void broadcastMessage(String string) {
		for(Player p : Bukkit.getOnlinePlayers())
			sendMessage(p, string, false);
	}
	
}
