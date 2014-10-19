package five.main.hub.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Serializer {

	public static String serializeLocation(Location l) {
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + ","
				+ l.getZ() + "," + l.getYaw() + "," + l.getPitch();
	}

	public static Location deserializeLocation(String s) {
		return new Location(
				Bukkit.getWorld(s.split(",")[0]),
				Double.parseDouble(s.split(",")[1]), 
				Double.parseDouble(s.split(",")[2]),
				Double.parseDouble(s.split(",")[3]),
				Float.parseFloat(s.split(",")[4]), 
				Float.parseFloat(s.split(",")[5])  );
	}

}
