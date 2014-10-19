package five.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import five.manager.MessageManager;

public class FiveCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender,Command paramCommand, String c,String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("You must use these commands as a player! Please go in-game!");
			return false;
		}
		Player p = (Player) sender;
		if(!p.isOp()){
			MessageManager.sendMessage(p, "You don't have permission to do that!", false);
			return false;
		}
		if(args.length == 0){
			MessageManager.sendMessage(p, "/5d <coins|ban>", false);
			return false;
		}
		if(args[0].equalsIgnoreCase("coins"))
			return CoinCommand.main(p, args);
		if(args[0].equalsIgnoreCase("ban"))
			return BanCommand.main(p,args);
		
		return false;
	}

}
