package five.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import five.cmd.FiveCommand;
import five.main.currency.Currency;
import five.main.hub.listener.ChatListener;
import five.main.hub.listener.CoinListener;
import five.main.hub.listener.HubListener;
import five.main.hub.listener.JoinListener;
import five.main.hub.listener.LeaveListener;
import five.main.hub.util.LobbyManager;
import five.main.hub.util.Serializer;
import five.main.hub.util.UUIDFetcher;
import five.main.ranks.Rank;

/**
 * Main class for all Fifth Dimension plugins.
 * @author Phase
 * @since 5d v1
 */
public class FifthDimension extends JavaPlugin {

	private static FifthDimension plugin;
	static Currency currency;
	private static ProtocolManager manager;
	
	private static Location joinLocation;
	private static Location spawnLocation;
	private static World MainWorld;
	
	private static Set<Player> sleeping = Collections.newSetFromMap(new WeakHashMap<Player, Boolean>());
	public static HashMap<UUID, Integer> HubScoreboardIds = new HashMap<UUID, Integer>();
	
	File configFile;
	static FileConfiguration config;
	
	/**
	 * Registers all the listeners.
	 * @author Phase
	 * @since 5d v1
	 */
	private void registerListeners() {
		register(new JoinListener());
		register(new HubListener());
		register(new ChatListener());
		register(new LeaveListener());
		register(new CoinListener());
	}
	
	/*** Base onEnable() method */
	public void onEnable(){
		plugin = this;
		currency = new Currency();
		
		manager = ProtocolLibrary.getProtocolManager();
		configFile = new File(getFilePath()+"config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		
		setupServerList();
		if(!config.contains("joinLocation"))
			config.set("joinLocation", Serializer.serializeLocation(new Location(Bukkit.getWorld("world"), 657.5, 100, -419.5, 0, 0)));
		joinLocation = Serializer.deserializeLocation(config.getString("joinLocation"));
		
		if(!config.contains("spawnLocation"))
			config.set("spawnLocation", Serializer.serializeLocation(new Location(Bukkit.getWorld("world"), 657.5, 58, -419.5, 180, 0)));
		spawnLocation = Serializer.deserializeLocation(config.getString("spawnLocation"));
		
		if(!config.contains("MainWorld"))
			config.set("MainWorld", "world");
		MainWorld = Bukkit.getWorld(config.getString("MainWorld"));
		
		saveYaml(config, configFile);
		Rank.startUp();
		Currency.startUp();
		BanSystem.startUp();
		registerListeners();
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getWorld().equals(getMainWorld())){
				giveHubScoreboard(p);
				LobbyManager.giveRandomLobby(p);
			}
		getCommand("5d").setExecutor(new FiveCommand());
	}
	
	private void setupServerList(){
		ProtocolLibrary.getProtocolManager().addPacketListener(
		        new PacketAdapter(PacketAdapter.params(this, PacketType.Status.Server.OUT_SERVER_INFO).optionAsync()) {
		            @Override
		            public void onPacketSending(PacketEvent e) {
		                final WrappedServerPing s = e.getPacket().getServerPings().read(0);
		                if(s.getPlayersOnline() == 0)
		                	s.setPlayersOnline(-9001);
		                s.setPlayersMaximum(100000);
		                ChatColor c = getRandomColor();
		                s.setMotD(c+"§lThe "+ChatColor.GREEN+"§l5"+c+"§lth Dimension§r §7§l|§r §a§k23§r §d§lTons of games! §a§k23§r");
		                UUID u = null;
		                try {u = UUIDFetcher.getUUIDOf("Phasesaber");}
		                catch(Exception e1) {}
		                if(u != null){
		                	s.setPlayers(Arrays.asList(
		                			w(u, "&d&lThe &a&l5&d&lth Dimension"),
		                			w(u, "&6&lMinigames:"),
		                			w(u, "&7-&3 Survivalist")
		                	));
		                }
		                
		            }
		        });
	}
	
	private WrappedGameProfile w(UUID u, String s){
		return new WrappedGameProfile(u, ChatColor.translateAlternateColorCodes('&', s));
	}
	
	public static ChatColor getRandomColor(){
		Random r = new Random();
		int i = r.nextInt(10)+1;
		switch(i){
			case 1: return ChatColor.RED;
			case 2: return ChatColor.LIGHT_PURPLE;
			case 3: return ChatColor.BLUE;
			case 4: return ChatColor.DARK_PURPLE;
			case 5: return ChatColor.GREEN;
			case 6: return ChatColor.GOLD;
			case 7: return ChatColor.AQUA;
			case 8: return ChatColor.YELLOW;
			case 9: return ChatColor.WHITE;
			case 10:return ChatColor.GRAY;
			default:return ChatColor.GREEN;
		}
		
	}
	
	/*** Base onDisable() method*/
	public void onDisable(){
		Currency.save();
		BanSystem.shutDown();
	}
	
	/**
	 * Where the player joins the game at.
	 * @author Phase
	 * @since 5d v1
	 * @return The Join Location
	 */
	public static Location getJoinLocation(){
		return joinLocation;
	}
	
	/**
	 * The official spawn location.
	 * @author Phase
	 * @since 5d v1
	 * @return The Spawn Location
	 */
	public static Location getSpawnLocation(){
		return spawnLocation;
	}
	
	/**
	 * Gives an instance of the main class.
	 * @return instance of the plugin & class
	 */
	public static FifthDimension getPlugin(){
		return plugin;
	}
	
	/**
	 * Quick method to register listeners.
	 */
	public void register(Listener l){
		Bukkit.getPluginManager().registerEvents(l, this);
	}
	
	/**
	 * For use in custom Yaml files.
	 *<p>
	 * <b>Ex:</b>
	 * <code>File f = new File(getFilePath+"file.yml");</code>
	 * @author Phase
	 * @since 5d v1
	 * @return "FifthDimension/"
	 */
	public static String getFilePath(){
		return "FifthDimension/";
	}
	/**
	 * @author Phase
	 * @param config Yaml config that is being saved.
	 * @param file File to save Config to.
	 * @return If saving was succesful
	 */
	public static boolean saveYaml(FileConfiguration config, File file){
		try {
			config.save(file);
			return true;
		} catch (IOException ex) {
			System.out.println("[FifthDimension] Could not save " + config + " to " + file + ".");
			return false;
		}
	}
	
	/**
	 * Gives the Hub Scoreboard to the player.
	 * <i>Used for after minigames end.</i>
	 * @param p Player to givethe scoreboard to.
	 * @return The scoreboard the player is given.
	 */
	public static Scoreboard giveHubScoreboard(Player p){
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		s.registerNewObjective("hub_objective", "dummy");
		Objective o = s.getObjective("hub_objective");
		o.setDisplayName("§d§lThe §a§l5§d§lth Dimension");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		giveObjectives(o,    "  ",
				"§aWelcome to the",
				"§a5th Dimension!",
				" ",
				"§c§lCoins:",
				"§d§l" + currency.getCoins(p),
				"§c",
				"§6Enjoy your",
				"§6stay!"
				);
		changeText(p,o, 4, 
				"§d§lWelcome to",
				"§d§lWelcome to", 
				"§d§lelcome to ", 
				"§d§llcome to T",
				"§d§lcome to Th", 
				"§d§lome to The", 
				"§d§lme to The ", 
				"§d§le to The §a§l5§d§l",
				"§d§l to The §a§l5§d§lt", 
				"§d§lto The §a§l5§d§lth", 
				"§d§lo The §a§l5§d§lth ", 
				"§d§l The §a§l5§d§lth D",
				"§d§lThe §a§l5§d§lth Di", 
				"§d§lThe §a§l5§d§lth Dim", 
				"§d§lThe §a§l5§d§lth Dime", 
				"§d§lThe §a§l5§d§lth Dimen",
				"§d§lThe §a§l5§d§lth Dimens", 
				"§d§lThe §a§l5§d§lth Dimensi", 
				"§d§lThe §a§l5§d§lth Dimensio",
				"§d§lThe §a§l5§d§lth Dimension", 
				"§d§lThe §a§l5§d§lth Dimension",
				"§d§lThe §a§l5§d§lth Dimension", 
				"§d§lThe §a§l5§d§lth Dimensio",
				"§d§lThe §a§l5§d§lth Dimensi", 
				"§d§lThe §a§l5§d§lth Dimens",
				"§d§lThe §a§l5§d§lth Dimen",
				"§d§lThe §a§l5§d§lth Dime",
				"§d§lThe §a§l5§d§lth Dim",
				"§d§lThe §a§l5§d§lth Di",
				"§d§lThe §a§l5§d§lth D",
				"§d§lThe §a§l5§d§lth ", 
				"§d§lThe §a§l5§d§lth", 
				"§d§lThe §a§l5§d§lt", 
				"§d§lThe §a§l5", 
				"§d§lThe ", 
				"§d§lThe", 
				"§d§lTh", 
				"§d§lT", 
				"",
				"",
				"",
				"§6§lEnjoy your",
				"§6§lEnjoy your",
				"§6§lnjoy your ",
				"§6§ljoy your s",
				"§6§loy your st",
				"§6§ly your sta",
				"§6§l your stay",
				"§6§lyour stay!",
				"§6§lour stay! ",
				"§6§lur stay!  ",
				"§6§lr stay!   ",
				"§6§l stay!    ",
				"§6§lstay!     ",
				"§6§ltay!      ",
				"§6§lay!       ",
				"§6§ly!        ",
				"§6§l!         ",
				"",
				""
				
				);
		p.setScoreboard(s);
		return s;
	}
	
	static int changeText = 0;

	public static void changeText(Player p, final Objective o, long time, final String... text) {
		if (!HubScoreboardIds.containsKey(p.getUniqueId())) {
			int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
					new Runnable() {
						public void run() {
							if (changeText >= text.length)
								changeText = 0;
							o.setDisplayName(text[changeText]);
							changeText++;
						}
					}, 0, time);
			HubScoreboardIds.put(p.getUniqueId(), id);
		}
	}
	
	public static void giveObjectives(Objective o, String... strings){
		int i = strings.length;
		for(int j = 0; i > j; i--)
			o.getScore(strings[i-1]).setScore(strings.length-i+1);
	}
	
	
	/** Gives the Hub World
	 * @return Hub World
	 */
	public static World getMainWorld() {
		return MainWorld;
	}

	@SuppressWarnings("deprecation")
	public static boolean hasPlayerBefore(Player p) {
		return Bukkit.getOfflinePlayer(p.getName()).hasPlayedBefore();
	}
	
	/**
	 * Creates a new Config File in the FifthDimension Folder.
	 * @param fileName - Name of Config File. Use: "test" Not: "test.yml"
	 * @return The FileConfiguration of the file.
	 */
	public static FileConfiguration createConfigFile(String fileName){
		File f = new File(getFilePath()+fileName+".yml");
		return YamlConfiguration.loadConfiguration(f);
	}
	
	public static void playSleepAnimation(Player asleep) {
        final PacketContainer bedPacket = manager.createPacket(PacketType.Play.Server.BED, false);
        final Location loc = asleep.getLocation();
        
        // http://wiki.vg/Protocol#Use_Bed
        bedPacket.getEntityModifier(asleep.getWorld()).
            write(0, asleep);
        bedPacket.getIntegers().
            write(1, loc.getBlockX()).
            write(2, loc.getBlockY() + 3).
            write(3, loc.getBlockZ());
        
        broadcastNearby(asleep, bedPacket);
        sleeping.add(asleep);
    }
    
    public static void stopSleepAnimation(Player sleeping) {
        final PacketContainer animation = manager.createPacket(PacketType.Play.Server.ANIMATION, false);
        
        // http://wiki.vg/Protocol#Animation
        animation.getEntityModifier(sleeping.getWorld()).
            write(0, sleeping);
        animation.getIntegers().
            write(1, 2);
        
        broadcastNearby(sleeping, animation);
        FifthDimension.sleeping.remove(sleeping);
    }
 
    private static void broadcastNearby(Player asleep, PacketContainer bedPacket) {
        for (Player observer : manager.getEntityTrackers(asleep)) {
            try {
                manager.sendServerPacket(observer, bedPacket);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot send packet.", e);
            }
        }
    }

	public static Set<Player> getSleepingPlayers(){ 
		return FifthDimension.sleeping;
	}
}
