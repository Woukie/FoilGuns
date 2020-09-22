package foilGuns;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FoilListener implements org.bukkit.event.Listener{
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
		GunMain gunManager = new GunMain();
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
        	
        	Player sender = event.getPlayer();
        	ItemStack potentialGun = event.getPlayer().getItemInHand();
        	
        	//if the item the player is holding is associated with a bullet emiter, shoot the bullet
        	if (gunManager.doesThisGunExist(potentialGun)) {
        		event.setCancelled(true);
        		gunManager.shootBullet(potentialGun, sender);
        	}
        }
    }
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event instanceof Player) {
			
		}
	}
}
