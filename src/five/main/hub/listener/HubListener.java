package five.main.hub.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import five.main.FifthDimension;
import five.main.hub.util.Door;
import five.main.hub.util.Door.NonExistantDoorException;
import five.main.hub.util.LobbyManager;
import five.manager.MessageManager;

public class HubListener implements Listener {
	
	public boolean isInHub(Player p){
		return p.getWorld().equals(FifthDimension.getMainWorld());
	}
	
	@EventHandler
	public void LobbyUtils(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(!isInHub(p)) return;
		e.setCancelled(true);
		if(p.getItemInHand().getType().equals(Material.WATCH)){
			LobbyManager.openInventory(p);
		}
		else if(p.getItemInHand().getType().equals(Material.BED)){
			if(FifthDimension.getSleepingPlayers().contains(p)){
				FifthDimension.stopSleepAnimation(p);
				MessageManager.sendMessage(p, "Sleeping toggled off.", false);
			}
			else{
				FifthDimension.playSleepAnimation(p);
				MessageManager.sendMessage(p, "Sleeping toggled on.", false);
			}
			
		}
	}
	
	@EventHandler
	public void ChangeLobby(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getName().equalsIgnoreCase(LobbyManager.InventoryName)){
			e.setCancelled(true);
			if(e.getCurrentItem() == null) return;
			if(e.getCurrentItem().getType().equals(Material.GOLD_BLOCK)) return;
			try{
				int i = Integer.parseInt(ChatColor.stripColor(
						e.getCurrentItem().getItemMeta().getDisplayName()).split(" ")[1]);
				LobbyManager.changeLobby(p, i, true);
				p.closeInventory();
			}catch(Exception error){}
		}
	}
	
	@EventHandler
	public void food(FoodLevelChangeEvent e){
		Player p = (Player)e.getEntity();
		if(isInHub(p))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void takeDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(isInHub(p))
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e){
		if(isInHub(e.getPlayer())){
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(e.getClickedBlock().getType().equals(Material.PISTON_MOVING_PIECE)){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e){
		if (isInHub(e.getPlayer()))
			if (e.getPlayer().isOp()){
				if (e.getBlock().getType() == Material.PISTON_MOVING_PIECE)
					e.setCancelled(true);
			}else
					e.setCancelled(true);
			
	}
	
	@EventHandler
	public void placeBlock(BlockPlaceEvent e){
		if(isInHub(e.getPlayer()) && !e.getPlayer().isOp())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void usePortal(PlayerPortalEvent e){
		if(isInHub(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void moveEvent(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(isInHub(p)){
			if(e.getTo().getBlock().getType() == Material.PISTON_MOVING_PIECE){
				Location l = FifthDimension.getJoinLocation();
				if((e.getTo().getBlockX() != l.getBlockX() && e.getTo().getBlockZ() != l.getBlockZ())
						|| (e.getTo().getBlockX() != l.getBlockX() && e.getTo().getBlockZ() != l.getBlockZ())){
					if(p.isOp()){
						if(!p.isSneaking())
							e.setCancelled(true);
					}
					else 
						e.setCancelled(true);
				}
			}
			for(int x = -2; x != 3; x++){
				for(int z = -2; z != 3; z++){
					Block b = p.getLocation().add(x, 0, z).getBlock();
					if(b.getType() == Material.IRON_DOOR || b.getType() == Material.IRON_DOOR_BLOCK){
						Door door = null;
						try { door = new Door(b.getLocation());} 
						catch (NonExistantDoorException e1) {e1.printStackTrace();}
						try {
							if(door != null){
								
								if(x / 2 == 1 || z / 2 == 1 || -x / 2 == 1 || -z / 2 == 1){
									if(!door.isClosed()){
										door.toggle(true);
									}
								}
								else if(x == 0 || z == 0){
									if(door.isClosed()){
										door.toggle(true);
									}
								}
								else{
									if(door.isClosed()){
										door.toggle(true);
									}
								}
							}
						} catch (NonExistantDoorException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
	
}
