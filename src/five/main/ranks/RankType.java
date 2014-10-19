package five.main.ranks;

import org.bukkit.entity.Player;


/**
 * Used for getting ranks for minigames & other plugins. 
 * @author Phase
 * @since 5d v2
 */
public enum RankType {
	
	PLAYER(0),VIP(1), ADMIN(2), OWNER(3), DEVELOPER(2);
	
	int permissionLevel;
	
	RankType(int permissionLevel){
		this.permissionLevel = permissionLevel;
	}
	
	/**
	 * @return permissionLevel - Level of perms the Rank has.(From 0-3)
	 */
	public int getPermLevel(){
		return permissionLevel;
	}
	
	/**
	 * Used for getting ranks.
	 * @param p The Player you want the RankType for.
	 * @return The Player's RankType
	 */
	public static RankType getRank(Player p){
		if(Rank.getOwners().contains(p.getUniqueId()))
			return OWNER;
		if(Rank.getDevelopers().contains(p.getUniqueId()))
			return DEVELOPER;
		if(Rank.getAdmins().contains(p.getUniqueId()))
			return ADMIN;
		if(Rank.getVips().contains(p.getUniqueId()))
			return VIP;
		return PLAYER;
	}
}
