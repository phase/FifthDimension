package five.main;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class Warp {
	
	
	File warpFile;
	FileConfiguration warps;
	
	public Warp(){
		warpFile = new File(FifthDimension.getFilePath() + "warps.yml");
		warps = YamlConfiguration.loadConfiguration(warpFile);
		FifthDimension.saveYaml(warps, warpFile);
	}
	
}
