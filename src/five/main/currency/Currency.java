package five.main.currency;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import five.main.FifthDimension;
import five.main.hub.util.UUIDFetcher;
import five.manager.MessageManager;

public class Currency {
	
	static HashMap<UUID, Integer> coins = new HashMap<UUID, Integer>();
	static Currency instance = new Currency();
	
	public static void startUp(){
		f = new File(FifthDimension.getFilePath()+"currency.yml");
		config = YamlConfiguration.loadConfiguration(f);
		if(!config.contains("Coins"))
			try {config.set("Coins." + UUIDFetcher.getUUIDOf("Phasesaber"), 1000);} 
			catch (Exception e) {e.printStackTrace();}
		for(String s : config.getConfigurationSection("Coins").getKeys(false)){
			coins.put(UUID.fromString(s), config.getInt("Coins."+s));
		}
		FifthDimension.saveYaml(config, f);
	}
	
	static File f;
	static FileConfiguration config;
	
	public static void save(){
		for(UUID u : coins.keySet())
			config.set("Coins." + u.toString(), coins.get(u));
		boolean didSave = FifthDimension.saveYaml(config, f);
		if(!didSave)
			System.out.println("5d: Couldn't save currency to the file!");
	}
	
	public void setCoins(Player p, int i){
		if(p.getWorld().equals(FifthDimension.getMainWorld()))
			FifthDimension.giveHubScoreboard(p);
		coins.put(p.getUniqueId(), i);
	}
	
	public int getCoins(Player p){
		return coins.get(p.getUniqueId());
	}
	
	public boolean hasCoins(Player p){
		return coins.containsKey(p.getUniqueId());
	}
	
	public void giveCoins(Player p, int amount, String reason, boolean achievementGet){
		if(achievementGet)
			MessageManager.sendMessage(p, "§8§lAchievement Get!", false);
		MessageManager.sendMessage(p, "§f§l+" + amount+" §7for " + "§a§l" + reason, false);
		setCoins(p, getCoins(p)+amount);
		if(p.getWorld().equals(FifthDimension.getMainWorld()))
			FifthDimension.giveHubScoreboard(p);
		MessageManager.sendMessage(p, "§d§lTotal Coins§f§l:§5§l " + getCoins(p), false);
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 5);
		try{TimeUnit.MILLISECONDS.sleep(200);} 
		catch(InterruptedException e) {System.out.println("Fail");}
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 3);
		try{TimeUnit.MILLISECONDS.sleep(200);} 
		catch(InterruptedException e) {System.out.println("Fail");}
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1);
	}
	
	public static Currency getInstance(){
		return instance;
	}
}
