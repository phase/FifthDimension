package five.main.hub.listener;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import five.manager.MessageManager;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onPlayerChatTabComplete( PlayerChatTabCompleteEvent event) {
	    final String token = event.getLastToken();
	    if(token.startsWith("@")) {
	        Collection<String> autoCompletions = event.getTabCompletions();
	        autoCompletions.clear();
	        String begin = token.replaceAll("@", "").toLowerCase();
	        for(Player player : Bukkit.getOnlinePlayers()) {
	            String playerName = player.getName();
	            if(playerName.toLowerCase().startsWith(begin)) {
	                autoCompletions.add("@" + playerName);
	            }
	        }
	    }
	}
	 
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
	    for(Player player : Bukkit.getOnlinePlayers()) {
	        String message = event.getMessage();
	        String playerName = "@" + player.getName();
	        if(message.contains(playerName)) {
	            event.setMessage(message.replaceAll(playerName, ChatColor.GREEN + playerName + "§7"));
	            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A));
	        }
	    }
	    String m = event.getMessage();
		String[] ms = m.split(" ");
		for(String s: ms)
			if(s.contains("#"))
				event.setMessage(m.replace(s, "§b"+s+"§7"));
		event.setCancelled(true);
		MessageManager.chatMessage(event.getPlayer(), event.getMessage());
		
	}
	
}
