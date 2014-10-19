package five.main.hub.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import five.main.FifthDimension;
import five.main.currency.Currency;

public class CoinListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void DropParty(PlayerPickupItemEvent e) {
		final Player p = e.getPlayer();
		final Item i = e.getItem();
		if (p.getWorld().equals(FifthDimension.getMainWorld())) {
			if (i.getItemStack().getType() == Material.INK_SACK
					&& i.getItemStack().getData().getData() == (byte) 10) {
				i.teleport(new Location(FifthDimension.getMainWorld(), 0, -1, 0));
				Currency c = new Currency();
				c.setCoins(p, c.getCoins(p)+1);
				e.setCancelled(true);
			}
		}
	}

}
