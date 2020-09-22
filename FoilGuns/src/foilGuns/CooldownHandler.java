package foilGuns;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import main.Main;

public class CooldownHandler {
	private boolean canShoot = true;
	public int miliSeconds = 1;
	BukkitScheduler scheduler = Bukkit.getScheduler();
	
	public void startTimer() {
		if (canShoot) {
			canShoot = false;
			scheduler.scheduleSyncDelayedTask(Main.plugin, new Runnable(){
				@Override
				public void run() {
					canShoot = true;
				}
			}, miliSeconds);			
		}
	}
}
