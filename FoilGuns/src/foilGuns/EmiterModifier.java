package foilGuns;

import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class EmiterModifier {
	GunMain gunManager = new GunMain();
	
	//must use a middle class between setting values and bullet emmiter as the right emmiter must be found from the hashmap
	
	public void setOffset(ItemStack gun, double x, double y, double z) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setOffset(x, y, z);
	}
	
	public void setTravelParticles(ItemStack gun, Particle particle, double particleSpread, int particleCount, double particleSpeed) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setTravelParticles(particle, particleSpread, particleCount, particleSpeed);
	}
	
	public void setHitParticles(ItemStack gun, Particle particle, double particleSpread, int particleCount, double particleSpeed) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setHitParticles(particle, particleSpread, particleCount, particleSpeed);
	}
	
	public void setShootParticles(ItemStack gun, Particle particle, double particleSpread, int particleCount, double particleSpeed) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setShootParticles(particle, particleSpread, particleCount, particleSpeed);
	}
	
	public void setBulletCooldown(ItemStack gun, double coolDown) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setBulletCooldown(coolDown);
	}
	
	public void setBulletTravelDistance(ItemStack gun, double distance) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setBulletTravelDistance(distance);
	}
	
	public void setBulletReflectionInfo(ItemStack gun, double probability, double degrees) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setBulletReflectionInfo(probability, degrees);
	}
	
	public void setBulletDamage(ItemStack gun, int bulletDamage) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setEntityHurtInfo(bulletDamage);
	}
	
	public void setBulletClipSize(ItemStack gun, int clipSize) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setBulletClipSize(clipSize);
	}
	
	public void setAutomatic(ItemStack gun, boolean isAutomatic, double startUpTime) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setAutomatic(isAutomatic, startUpTime);
	}
	
	public void setCritData(ItemStack gun, double critChance, double damageMultiplier) {
		BulletEmiter bulletEmitter = gunManager.getBulletEmiter(gun);
		if (!bulletEmitter.equals(null))
		bulletEmitter.setCritData(critChance, damageMultiplier);
	}
	
	public void setPierce(ItemStack gun, int pierce) {
		BulletEmiter bulletEmiter = gunManager.getBulletEmiter(gun);
		if (!bulletEmiter.equals(null))
		bulletEmiter.setPierces(pierce);
	}
	
	public void setBulletConsistency(int bulletConsistency) {
		BulletEmiter.globalBulletConsistency = bulletConsistency;
	}
}
