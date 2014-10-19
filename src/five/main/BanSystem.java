package five.main;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import five.main.hub.util.UUIDFetcher;

public class BanSystem {
	
	
	static HashMap<UUID,String> bannedPlayers = new HashMap<UUID,String>();
	
	public static Set<UUID> getBannedPlayers(){
		return bannedPlayers.keySet();
	}
	
	public static void banPlayer(UUID u, String reason){
		bannedPlayers.put(u, reason);
	}
	
	public static boolean isBanned(UUID u){
		return bannedPlayers.containsKey(u);
	}
	
	public static String getReason(UUID u){
		if(isBanned(u))
			return bannedPlayers.get(u);
		else
			return null;
	}
	
	static FileConfiguration config;
	static File f = new File(FifthDimension.getFilePath()+"banSystem.yml");
	
	public static void startUp(){
		config = FifthDimension.createConfigFile("banSystem");
		
		if(config == null)
			config = YamlConfiguration.loadConfiguration(f);
		
		if(config.contains("Banned-Players")){
			for(String s : config.getConfigurationSection("Banned-Players").getKeys(false)){
				bannedPlayers.put(UUID.fromString(s), config.getString("Banned-Players."+s+".Reason"));
			}
		}else{
			config.createSection("Banned-Players");
			try{
				//Found on MCBans
				bannedPlayers.put(UUIDFetcher.getUUIDOf("filbert80906"), "Spam of cussing, hacking, and griefing on 192.210.194.70:25865");
				bannedPlayers.put(UUIDFetcher.getUUIDOf("rcw43"), "Massive Grief on nyminecraft.com");
			}
			catch(Exception e){System.out.println("FifthDimension > Trouble adding people to ban system.");}
		}
	}
	
	public static void shutDown(){
		for(UUID u : bannedPlayers.keySet()){
			if(u != null) {
				if(config == null)
					config = YamlConfiguration.loadConfiguration(f);
				config.set("Banned-Players."+u.toString()+".Reason",bannedPlayers.get(u)+"");
			}
		}
		FifthDimension.saveYaml(config, new File(FifthDimension.getFilePath()+"banSystem.yml"));
	}
	
	
	
}
