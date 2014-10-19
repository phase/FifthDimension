package five.cmd;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import five.main.currency.Currency;
import five.manager.MessageManager;

public class CoinCommand {
	
	@SuppressWarnings("deprecation")
	public static boolean main(Player p, String[] args){
		if(args.length == 1){
			MessageManager.sendMessage(p, "/5d coins give <amount> [player]", false);
			return false;
		}
		else if(args[1].equalsIgnoreCase("give")){
			if(args.length == 3){
				int amount = 0;
				try{
					amount = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					MessageManager.sendMessage(p, "/5d coins give <amount> [player]", false);
					return false;
				}
				Currency.getInstance().giveCoins(p, amount, "Gift from yourself.", false);
			}
			else if(args.length == 4){
				int amount = 0;
				try{
					amount = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					MessageManager.sendMessage(p, "/5d coins give <amount> [player]", false);
					return false;
				}
				Player n = Bukkit.getPlayer(args[3]);
				if(n != null){
					if(n == p){
						Currency.getInstance().giveCoins(p, amount, "Gift from yourself.", false);
					}else{
						Currency.getInstance().giveCoins(n, amount, "Gift from "+p.getName()+".", false);
					}
				}else{
					MessageManager.sendMessage(p, "/5d coins give <amount> [player]", false);
					return false;
				}
			}
			else{
				MessageManager.sendMessage(p, "/5d coins give <amount> [player]", false);
				return false;
			}
		}else{
			MessageManager.sendMessage(p, "/5d coins give <amount> [player]", false);
			return false;
		}
		return false;
	}
	
	
}
