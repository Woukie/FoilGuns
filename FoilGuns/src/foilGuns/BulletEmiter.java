package foilGuns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

//a new emmiter exists for each gun
public final class BulletEmiter {
	//gun properties
	public static double globalBulletConsistency = 50; 
	
	private CooldownHandler cooldownHandler = new CooldownHandler();
	private List<Material> passThroughBlocks;
	
	private int maxAmmo = 10;
	private int damage = 0;
	private double reflectionAngle = 0;
	private double reflectionChance = 0;
	private double bulletTravelDistance = 20;
	private double bulletCooldown = 0;
	
	//hit data
	private double critChance = 0;
	private int pierces = 0;
	private double damageMultiplier = 1;
	
	//automatic data
	private Boolean automatic = false;
	private double gunStartUpTime = 0;
	
	//particles properties
	private Particle hitParticle = Particle.EXPLOSION_NORMAL;
	private double hitBulletWidth = 0;
	private int hitParticleCount = 0;
	private double hitParticleSpeed = 0;
	
	private Particle travelParticle = Particle.SUSPENDED_DEPTH;
	private double travelBulletWidth = 0;
	private int travelParticleCount = 1;
	private double travelParticleSpeed = 0;
	
	private Particle shootParticle = Particle.FLAME;
	private double shootParticleSpawnSpread = 0;
	private int shootParticleCount = 1;
	private double shootParticleSpeed = 0;
	
	//emitter offset data
	private Vector offset = new Vector(-0.1, -0.2, 0);
	
	//bullet data
	HashMap<Entity, Integer> damageEntityHashmap = new HashMap<Entity, Integer>();
	
	public BulletEmiter(ItemStack gun) {
		List<Material> passableBlocks = new ArrayList<Material>();
		
		passableBlocks.add(Material.AIR);
		passableBlocks.add(Material.CAVE_AIR);
		passableBlocks.add(Material.VOID_AIR);
		
		passableBlocks.add(Material.GRASS);
		passableBlocks.add(Material.TALL_GRASS);
		
		passableBlocks.add(Material.WATER);
		
		passableBlocks.add(Material.SNOW);
		
		this.passThroughBlocks = passableBlocks;
	}
	
	public void setOffset(double x, double y, double z) {
		x *= -1;
		
		offset = new Vector(x, y, z);
	}
	
	public void setTravelParticles(Particle particle, double particleSpread, int particleCount, double particleSpeed) {
		travelParticle = particle;
		travelBulletWidth = particleSpread;
		travelParticleCount = particleCount;
		travelParticleSpeed = particleSpeed;
	}
	
	public void setHitParticles(Particle particle, double particleSpread, int particleCount, double particleSpeed) {
		hitParticle = particle;
		hitBulletWidth = particleSpread;
		hitParticleCount = particleCount;
		hitParticleSpeed = particleSpeed;
	}
	
	public void setShootParticles(Particle particle, double particleSpread, int particleCount, double particleSpeed) {
		shootParticle = particle;
		shootParticleSpawnSpread = particleSpread;
		shootParticleCount = particleCount;
		shootParticleSpeed = particleSpeed;
	}
	
	public void setBulletCooldown(double coolDown) {
		bulletCooldown = coolDown;
	}
	
	public void setBulletTravelDistance(double distance) {
		bulletTravelDistance = distance;
	}
	
	public void setBulletReflectionInfo(double probability, double degrees) {
		if (probability >= 0 && probability <= 1) {
			reflectionChance = probability;
		}
		if (degrees <= 90 && degrees >= 0) {
			reflectionAngle = degrees;
		}
	}
	
	public void setEntityHurtInfo(int bulletDamage) {
		if (damage >= 0) {
			damage = bulletDamage;
		}
	}
	
	public void setBulletClipSize(int clipSize) {
		if (clipSize > 0) {
			maxAmmo = clipSize;
		}
	}
	
	public void setAutomatic(boolean isAutomatic, double startUpTime) {
		if (startUpTime >= 0) {
			gunStartUpTime = startUpTime;
		}
		automatic = isAutomatic;
	}
	
	public void setCritData(double critChance, double damageMultiplier) {
		if (critChance >= 0) {
			this.critChance = critChance;			
		}
		this.damageMultiplier = damageMultiplier;
	}
	
	public void setPassableBlocks(List<Material> materials) {
		if (materials.size() > 0) {
			passThroughBlocks = materials;
		}
	}
	
	public void setPierces(int number) {
		if (number >= 0) {
			pierces = number;
		}
	}
	
	public void fireBullet(Vector bulletDirection, Vector bulletStartLocation, World world, Player sender) {
		cooldownHandler.startTimer();
		bulletLocations.clear();
		buildBulletLocationsList(bulletDirection, bulletStartLocation, world, bulletTravelDistance);
		processEntities();
		applyOffset(world, sender);
		spawnParticles(world);
		hurtEntities();
	}
	
	List<List<Location>> bulletLocations = new ArrayList<List<Location>>();
	private void buildBulletLocationsList(Vector bulletDirection, Vector bulletStartLocation, World world, double thisBulletTravelDistance) {	
		//scan in direction of shot, record location of bullet and chunks crossed to damage entities.
		
		List<Location> thisBullet = new ArrayList<Location>();
		
		//build a list of locations the bullet will travel at
		for (double i = 0; i < thisBulletTravelDistance; i += 1 / globalBulletConsistency) {
			Vector returnVector = new Vector();
			returnVector.add(bulletDirection);
			returnVector.multiply(i).add(bulletStartLocation);
			
			Block passedBlock = returnVector.toLocation(world).getBlock();
			
			if (!passThroughBlocks.contains(passedBlock.getBlockData().getMaterial())) {
				
				bulletLocations.add(thisBullet);
				
				if (Math.random() < reflectionChance) {
					getNewShotDirection(thisBullet.get(thisBullet.size() - 1).toVector(), world, bulletDirection);
				}
				
				return;
			} else {
				thisBullet.add(returnVector.toLocation(world));				
			}
		}
		bulletLocations.add(thisBullet);
	}

	private void processEntities() {
		
		List<List<Location>> verifiedBulletLocations = new ArrayList<List<Location>>();
		damageEntityHashmap.clear();
		boolean cantContinue = false;
		
		
		//for every entity in the chunk the bullets in, check if the bullet hits the entity
		
		for (List<Location> locationList : bulletLocations) {
			
			List<Entity> verifiedEntities = new ArrayList<Entity>();
			List<Location> verifiedLocationList = new ArrayList<Location>();
			
			verifiedEntities.clear();
			
			if (!cantContinue) {
				for (Location location : locationList) {
					if (verifiedEntities.size() <= pierces) {
						
						verifiedLocationList.add(location);
						
						for (Entity potentialEntity : location.getChunk().getEntities()) {
							
							//only execute if the entity has not been hit (by this specific bullet) and is also not a player
							if (!verifiedEntities.contains(potentialEntity) && !(potentialEntity instanceof Player)) {
								
								//get information about the entity
								Double entityHalfWidth = potentialEntity.getWidth() / 2;
								Double entityHeight = potentialEntity.getHeight();
								
								double bulletX = location.getX();
								double bulletY = location.getY();
								double bulletZ = location.getZ();
								
								double entityX = potentialEntity.getLocation().getX();
								double entityY = potentialEntity.getLocation().getY();
								double entityZ = potentialEntity.getLocation().getZ();
								
								//info about wether the bullet intersects the entity
								boolean entityAlignsX = (entityX < bulletX + entityHalfWidth && entityX > bulletX - entityHalfWidth);
								boolean entityAlignsY = (entityY < bulletY && entityY + entityHeight > bulletY);
								boolean entityAlignsZ = (entityZ < bulletZ + entityHalfWidth && entityZ > bulletZ - entityHalfWidth);
								
								//add entity to entity list
								if (entityAlignsX && entityAlignsY && entityAlignsZ) {
									verifiedEntities.add(potentialEntity);
								}
							}
						}
					} else {
						cantContinue = true;
					}
				}
			}

			//add entities to hashmap
			for (Entity entity : verifiedEntities) {
				if (damageEntityHashmap.containsKey(entity)) {
					damageEntityHashmap.put(entity, damageEntityHashmap.get(entity) + damage);
				} else {
					damageEntityHashmap.put(entity, damage);
				}
			}
			
			if (verifiedLocationList.size() > 0)
			verifiedBulletLocations.add(verifiedLocationList);
		}
		bulletLocations = verifiedBulletLocations;
	}
	
	//takes in a list of second pass bullet locations and spawns the particles (uses offset)
	private void applyOffset(World world, Player sender) {
		
		//code for the offset (first list)
		List<Location> toOffsetLocations = bulletLocations.get(0);
		List<Location> offsettedLocations = new ArrayList<Location>();
		
		Vector playerEyeLocation = sender.getEyeLocation().toVector();
		
		Vector localisedOffset = new Vector();
		localisedOffset.add(offset);
		localisedOffset = rotateAxisY(localisedOffset, sender.getLocation().getYaw());
		
		Vector newBulletDirection = new Vector();
		newBulletDirection.add(toOffsetLocations.get(toOffsetLocations.size() - 1).toVector());
		newBulletDirection.subtract(playerEyeLocation);
		newBulletDirection.subtract(localisedOffset);
		newBulletDirection.normalize();
		
		Vector startLocation = new Vector();
		startLocation.add(localisedOffset);
		startLocation.add(playerEyeLocation);
		
		Vector startToEnd = new Vector();
		startToEnd = toOffsetLocations.get(toOffsetLocations.size() - 1).toVector().subtract(startLocation);
		
		double offsetToDestLength = Math.sqrt(Math.pow(startToEnd.getX(), 2) + Math.pow(startToEnd.getY(), 2) + Math.pow(startToEnd.getZ(), 2));
		
		for (double i = 0; i < offsetToDestLength; i += (1 / globalBulletConsistency)) {
			
			Vector returnVector = new Vector();
			
			returnVector.add(newBulletDirection);
			returnVector.multiply(i);
			
			returnVector.add(playerEyeLocation);
			returnVector.add(localisedOffset);
			
			
			offsettedLocations.add(returnVector.toLocation(world));
		}
		
		bulletLocations.set(0, offsettedLocations);
		//code for reflections (all other lists)
	}
	
	private void hurtEntities() {
		damageEntityHashmap.forEach((e, i) -> effectEntity(e, i));
	}
	
	private void effectEntity(Entity entity, Integer damage) {
		if (!entity.isDead()) {
			try {
				LivingEntity aliveEntity = (LivingEntity) entity;
				aliveEntity.setMaximumNoDamageTicks(0);
				double entityDamage = (double) damage;
				if (Math.random() <= critChance) {
					entityDamage *= damageMultiplier;
				}
				aliveEntity.damage(entityDamage);
				aliveEntity.setMaximumNoDamageTicks(20);
			} catch (Exception e) {
				
			}
		}
	}
	
	private void spawnParticles(World world) {
		Location shootParticle = bulletLocations.get(0).get(0);
		world.spawnParticle(this.shootParticle, shootParticle.getX(), shootParticle.getY(), shootParticle.getZ(), shootParticleCount, shootParticleSpawnSpread, shootParticleSpawnSpread, shootParticleSpawnSpread, shootParticleSpeed);
		
		for (List<Location> locationList : bulletLocations) {
			for (Location location : locationList) {
				world.spawnParticle(travelParticle, location.getX(), location.getY(), location.getZ(), travelParticleCount, travelBulletWidth, travelBulletWidth, travelBulletWidth, travelParticleSpeed);				
			}
		}
	
		List<Location> lastList = bulletLocations.get(bulletLocations.size() - 1);
		Location hitParticle = lastList.get(lastList.size() - 1);
		world.spawnParticle(this.hitParticle, hitParticle.getX(), hitParticle.getY(), hitParticle.getZ(), hitParticleCount, hitBulletWidth, hitBulletWidth, hitBulletWidth, hitParticleSpeed);
	}
	
	private void getNewShotDirection(Vector preIntersect, World world, Vector aproachDirection) {
		double PIdiv180 = Math.PI / 180;
		
		int currentTravelDistance = 0;
		for (List<Location> locationList : bulletLocations) {
			currentTravelDistance += locationList.size();
		}
		currentTravelDistance /= globalBulletConsistency;
		
		Vector rotatedAxis = new Vector();
		Vector intersectLocation = new Vector();
		Vector preIntersectLocation = new Vector();
		
		preIntersectLocation.add(preIntersect);
		
		intersectLocation.add(aproachDirection);
		intersectLocation.multiply(1 / globalBulletConsistency);
		intersectLocation.add(preIntersect);
		
		double BlockX = intersectLocation.getBlockX();
		double BlockY = intersectLocation.getBlockY();
		double BlockZ = intersectLocation.getBlockZ();
		
		double X1 = preIntersectLocation.getX();
		double Y1 = preIntersectLocation.getY();
		double Z1 = preIntersectLocation.getZ();
		
		double X2 = intersectLocation.getX();
		double Y2 = intersectLocation.getY();
		double Z2 = intersectLocation.getZ();
		
		double Mag = (BlockX - X1) / (X2 - X1);
		double intersectY = Y1 + Mag * (Y2 - Y1);
		double intersectZ = Z1 + Mag * (Z2 - Z1);
		
		if (Mag < 1 && Mag > 0 && intersectY > BlockY && intersectY < BlockY + 1 && intersectZ > BlockZ && intersectZ < BlockZ + 1) {
			if((aproachDirection.angle(new Vector(1, 0, 0)) / PIdiv180) >= reflectionAngle) {
				rotatedAxis = rotateAxisX(aproachDirection, 180).multiply(-1);
				buildBulletLocationsList(rotatedAxis, preIntersect, world, bulletTravelDistance - currentTravelDistance);				
			}
		}
		
		Mag = (BlockX + 1 - X1) / (X2 - X1);
		intersectY = Y1 + Mag * (Y2 - Y1);
		intersectZ = Z1 + Mag * (Z2 - Z1);
		
		if (Mag < 1 && Mag > 0 && intersectY > BlockY && intersectY < BlockY + 1 && intersectZ > BlockZ && intersectZ < BlockZ + 1) {
			if(aproachDirection.angle(new Vector(-1, 0, 0)) / PIdiv180 >= reflectionAngle) {
				rotatedAxis = rotateAxisX(aproachDirection, 180).multiply(-1);
				buildBulletLocationsList(rotatedAxis, preIntersect, world, bulletTravelDistance - currentTravelDistance);
			}
		}
		
		Mag = (BlockY - Y1) / (Y2 - Y1);
		double intersectX = X1 + Mag * (X2 - X1);
		intersectZ = Z1 + Mag * (Z2 - Z1);
		
		if (Mag < 1 && Mag > 0 && intersectX > BlockX && intersectX < BlockX + 1 && intersectZ > BlockZ && intersectZ < BlockZ + 1) {
			if(aproachDirection.angle(new Vector(0, 1, 0)) / PIdiv180 >= reflectionAngle) {
				rotatedAxis = rotateAxisY(aproachDirection, 180).multiply(-1);
				buildBulletLocationsList(rotatedAxis, preIntersect, world, bulletTravelDistance - currentTravelDistance);
			}
		}
		
		Mag = (BlockY + 1 - Y1) / (Y2 - Y1);
		intersectX = X1 + Mag * (X2 - X1);
		intersectZ = Z1 + Mag * (Z2 - Z1);
		
		if (Mag < 1 && Mag > 0 && intersectX > BlockX && intersectX < BlockX + 1 && intersectZ > BlockZ && intersectZ < BlockZ + 1) {
			if(aproachDirection.angle(new Vector(0, -1, 0)) / PIdiv180 >= reflectionAngle) {
				rotatedAxis = rotateAxisY(aproachDirection, 180).multiply(-1);
				buildBulletLocationsList(rotatedAxis, preIntersect, world, bulletTravelDistance - currentTravelDistance);
			}
		}
		
		Mag = (BlockZ - Z1) / (Z2 - Z1);
		intersectY = Y1 + Mag * (Y2 - Y1);
		intersectX = X1 + Mag * (X2 - X1);
		
		if (Mag < 1 && Mag > 0 && intersectX > BlockX && intersectX < BlockX + 1 && intersectY > BlockY && intersectY < BlockY + 1) {
			if(aproachDirection.angle(new Vector(0, 0, 1)) / PIdiv180 >= reflectionAngle) {
				rotatedAxis = rotateAxisZ(aproachDirection, 180).multiply(-1);
				buildBulletLocationsList(rotatedAxis, preIntersect, world, bulletTravelDistance - currentTravelDistance);
			}
		}
		
		Mag = (BlockZ + 1 - Z1) / (Z2 - Z1);
		intersectY = Y1 + Mag * (Y2 - Y1);
		intersectX = X1 + Mag * (X2 - X1);
		
		if (Mag < 1 && Mag > 0 && intersectX > BlockX && intersectX < BlockX + 1 && intersectY > BlockY && intersectY < BlockY + 1) {
			if(aproachDirection.angle(new Vector(0, 0, -1)) / PIdiv180 >= reflectionAngle) {
				rotatedAxis = rotateAxisZ(aproachDirection, 180).multiply(-1);
				buildBulletLocationsList(rotatedAxis, preIntersect, world, bulletTravelDistance - currentTravelDistance);
			}
		}
	}
	
	private Vector rotateAxisX(Vector vector, double angle) {
		double vecY = vector.getY();
		double vecZ = vector.getZ();
		
		angle *= (Math.PI / 180);
		
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		vector.setY(vecY * cos - vecZ * sin);
		vector.setZ(vecY * sin + vecZ * cos);
		
		return vector;
	}
	
	private Vector rotateAxisY(Vector vector, double angle) {
		double vecX = vector.getX();
		double vecZ = vector.getZ();
		
		angle *= (Math.PI / 180);
		
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		vector.setX(vecX * cos - vecZ * sin);
		vector.setZ(vecX * sin + vecZ * cos);
		
		return vector;
	}

	private Vector rotateAxisZ(Vector vector, double angle) {
		double vecX = vector.getX();
		double vecY = vector.getY();

		angle *= (Math.PI / 180);
		
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		vector.setX(vecX * cos - vecY * sin);
		vector.setY(vecX * sin + vecY * cos);
		
		return vector;
	}
	
}