package five.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import five.main.BanSystem;
import five.main.hub.util.NameFetcher;
import five.main.hub.util.UUIDFetcher;
import five.main.ranks.RankType;
import five.manager.MessageManager;

public class BanCommand {

	@SuppressWarnings({ "deprecation"})
	public static boolean main(Player p, String[] args) {
		if(args.length == 1){
			MessageManager.sendMessage(p, "/5d ban <player> [reason]", false);
			return false;
		}
			if(args[1].equalsIgnoreCase("list")){
				MessageManager.sendMessage(p, "List of Banned players:", false);
				List<UUID> l = new ArrayList<UUID>();
				for(UUID u : BanSystem.getBannedPlayers())
					if(u != null)
					l.add(u);
				NameFetcher nameF = new NameFetcher(l);
				Map<UUID, String> names = null;
				try{names = nameF.call();}
				catch(Exception e){e.printStackTrace();}
				if(names != null)
				for(UUID u : l){
					MessageManager.sendMessage(p, names.get(u) + ": " + BanSystem.getReason(u), false);
				}
				return true;
			}
			else if(Bukkit.getPlayer(args[1]) != null){
				Player n = Bukkit.getPlayer(args[1]);
				if(args.length > 2){
					String reason = "";
					for(int i = 2; i < args.length; i++)
						reason += args[i]+" ";
					BanSystem.banPlayer(n.getUniqueId(), reason);
					n.kickPlayer(reason);
				}
				else{
					BanSystem.banPlayer(n.getUniqueId(), "No reason specified!");
					n.kickPlayer("No reason specified!");
				}
				MessageManager.sendMessage(p, "Player " + args[1] + " has been banned!",false);
				
			}else{
				UUID u = null;
				try {
					u = UUIDFetcher.getUUIDOf(args[1]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(u == null){
					MessageManager.sendMessage(p,"Player not found!",false);
					if(RankType.getRank(p).getPermLevel() > 2)
						MessageManager.sendMessage(p, "Player " + args[1] + " with the UUID of " + u + " could not be found.", false);
				}else{
					if(args.length > 2){
						String reason = "";
						for(int i = 2; i < args.length; i++)
							reason += args[i]+" ";
						BanSystem.banPlayer(u, reason);
					}
					else
						BanSystem.banPlayer(u, "No reason specified!");
					MessageManager.sendMessage(p, "Player " + args[1] + " has been banned!",false);
				}
			}
		
		return false;
	}
	
	
	
	
}
