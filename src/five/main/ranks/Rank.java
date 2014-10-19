package five.main.ranks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import five.main.FifthDimension;
import five.main.hub.util.UUIDFetcher;

public class Rank {
	
	static File f;
	static FileConfiguration config;
	
	private static List<String> dVip = new ArrayList<String>();
	private static List<String> dAdmin = new ArrayList<String>();
	private static List<String> dOwner = new ArrayList<String>();
	private static List<String> dDev = new ArrayList<String>();
	
	static List<UUID> vip = new ArrayList<UUID>();
	static List<UUID> admin = new ArrayList<UUID>();
	static List<UUID> owner = new ArrayList<UUID>();
	static List<UUID> developer = new ArrayList<UUID>();
	
	public static void startUp(){
		f = new File(FifthDimension.getFilePath()+"ranks.yml");
		config = YamlConfiguration.loadConfiguration(f);
		
		try {
			dVip.add(UUIDFetcher.getUUIDOf("frittenthekitten")+"");
			dAdmin.add(UUIDFetcher.getUUIDOf("battlebears10")+"");
			dOwner.add(UUIDFetcher.getUUIDOf("untoldlegend1")+"");
			dOwner.add(UUIDFetcher.getUUIDOf("Phasesaber")+"");
			dDev.add(UUIDFetcher.getUUIDOf("Phasesaber")+"");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		if(!config.contains("Ranks.Vip"))
			config.set("Ranks.Vip", dVip);
		
		for(String s : config.getStringList("Ranks.Vip")){
			try{
				vip.add(UUID.fromString(s));
			}catch(IllegalArgumentException e){
				System.out.println("[FifthDimension] No players to add to Rank.Vip!");
			}
		}	
		if(!config.contains("Ranks.Admin"))
			config.set("Ranks.Admin", dAdmin);
		
		for(String s : config.getStringList("Ranks.Admin")){
			try{
				admin.add(UUID.fromString(s));
			}catch(IllegalArgumentException e){
				System.out.println("[FifthDimension] No players to add to Rank.Admin!");
			}
		}	
		if(!config.contains("Ranks.Owner"))
			config.set("Ranks.Owner", dOwner);
		
		for(String s : config.getStringList("Ranks.Owner")){
			try{
				owner.add(UUID.fromString(s));
			}catch(IllegalArgumentException e){
				System.out.println("[FifthDimension] No players to add to Rank.Owner!");
			}
		}
		if(!config.contains("Ranks.Developer"))
			config.set("Ranks.Developer", dDev);
		
		for(String s : config.getStringList("Ranks.Developer")){
			try{
			developer.add(UUID.fromString(s));
			}catch(IllegalArgumentException e){
				System.out.println("[FifthDimension] No players to add to Rank.Developer!");
			}
		}
		
		FifthDimension.saveYaml(config, f);
	}
	
	public static List<UUID> getVips(){
		return vip;
	}
	
	public static List<UUID> getAdmins(){
		return admin;
	}
	
	public static List<UUID> getOwners(){
		return owner;
	}

	public static List<UUID> getDevelopers(){
		return developer;
	}
	
	
	
	
	
	
	
	
	
	
}
