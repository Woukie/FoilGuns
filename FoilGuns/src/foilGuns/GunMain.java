package foilGuns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GunMain {
	private HashMap<ItemStack, BulletEmiter> guns = new HashMap<ItemStack, BulletEmiter>();
	private HashMap<Player, CooldownHandler> playersCooldown = new HashMap<Player, CooldownHandler>();
	
	//each gun in the guns hashmap is associated with a bulletEmiter
	public void setUpGun(ItemStack gunItem) {
		BulletEmiter emiter = new BulletEmiter(gunItem);
		if (!guns.containsKey(gunItem)) guns.put(gunItem, emiter);
	}
	
	public void deleteGun(ItemStack gunItem) {
		if (guns.containsKey(gunItem)) guns.remove(gunItem);
	}
	
	public void shootBullet(ItemStack gun, Player sender) {
		Vector fireLocation = sender.getEyeLocation().toVector();
		Vector fireDirection = sender.getEyeLocation().getDirection();
		
		guns.get(gun).fireBullet(fireDirection, fireLocation, sender.getWorld(), sender);
	}
	
	public List<ItemStack> getAllGunItems() {
		List<ItemStack> returnItemStack = new ArrayList<ItemStack>();
		
		for (ItemStack itemStack : guns.keySet()) {
			returnItemStack.add(itemStack);
		}
		
		return returnItemStack;
	}
	
	public boolean doesThisGunExist(ItemStack potentialGun) {
		if (guns.containsKey(potentialGun)) 
		return true;
		return false;
	}
	
	public BulletEmiter getBulletEmiter(ItemStack gun) {
		if (guns.containsKey(gun)) { 
			return guns.get(gun);
		} else {
			return null;
		}
	}
}