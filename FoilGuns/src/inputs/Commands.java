package inputs;

import java.util.List;

import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import foilGuns.EmiterModifier;
import foilGuns.GunMain;

@SuppressWarnings("deprecation")
public class Commands implements CommandExecutor {
	
	GunMain gunMain = new GunMain();
	EmiterModifier emiterModifier = new EmiterModifier();
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("gun") && sender instanceof Player) {
			Player player = (Player) sender;
			
			if (args.length < 1) {
				player.sendMessage("Proper Syntax: /gun [Args]");
			}
			
			
			if(args[0].equalsIgnoreCase("CREATE")) {
				if (args.length == 1) {
					ItemStack gun = player.getItemInHand();
					gunMain.setUpGun(gun);
					player.sendMessage("A gun is now associated with this tool");
				} else {
					player.sendMessage("Proper Syntax: /gun Create");
				}
			}
			
			if(args[0].equalsIgnoreCase("DELETE")) {
				
				if (args.length == 1) {
					ItemStack potentialGun = player.getItemInHand();
					
					if (!gunMain.doesThisGunExist(potentialGun)) {
						player.sendMessage("You must be holding a gun (Create a FoilGun by holding an item and doing /gun Create)");
						return true;
					}
					gunMain.deleteGun(potentialGun);
					player.sendMessage("All gun data about this item has been wiped");
				} else {
					player.sendMessage("Proper Syntax: /gun Delete");
				}
			}
			
			if(args[0].equalsIgnoreCase("LIST")) {
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("ALL")) {
						
						List<ItemStack> allGuns = gunMain.getAllGunItems();
						if (allGuns.size() > 0) {
							for (ItemStack itemStack : allGuns) {
								sender.sendMessage(itemStack.toString());
							}							
						} else {
							player.sendMessage("There are no guns to list");
						}
						
					} else if (args[1].equalsIgnoreCase("HOLDING")) {
						
						player.sendMessage("Getting information about held gun is restricted");
						
					} else {
						player.sendMessage("Proper Syntax: /gun List [All | Holding]");
					}
				} else {
					player.sendMessage("Proper Syntax: /gun List [All | Holding]");
				}
			}
			
			if(args[0].equalsIgnoreCase("EDIT")) {
				if (args.length >= 2) {
					if (args[1].equalsIgnoreCase("Offset")) {
						if (args.length == 5) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a Gun by holding an item and doing /gun Create)");
								return true;
							}
							
							Double X;
							Double Y;
							Double Z;
							
							try {
								X = Double.parseDouble(args[2]);
							} catch (Exception e) {
								player.sendMessage("X must be a number");
								return true;
							}
							
							try {
								Y = Double.parseDouble(args[3]);
							} catch (Exception e) {
								player.sendMessage("Y must be a number");
								return true;
							}
							
							try {
								Z = Double.parseDouble(args[4]);
							} catch (Exception e) {
								player.sendMessage("Z must be a number");
								return true;
							}
							
							emiterModifier.setOffset(potentialGun, X, Y, Z);
							player.sendMessage("Set offset");
							return true;
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit Offset [X | Y | Z]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("ParticlesTravel")) {
						if (args.length == 6) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a Gun by holding an item and doing /gun Create)");
								return true;
							}
							
							Particle passParticle = null;
							Double spread = null;
							Integer count = null;
							Double speed = null;
							
							try {
								passParticle = Particle.valueOf(args[2]);
							} catch (Exception e) {
								player.sendMessage("Particle must be a particle");
								return true;
							}
							
							try {
								spread = Double.parseDouble(args[3]);
							} catch (Exception e) {
								player.sendMessage("Spread must be a number greater than 0");
								return true;
							}

							try {
								count = Integer.parseInt(args[4]);
							} catch (Exception e) {
								player.sendMessage("Count must be an integer greater than or eaqual to 0");
								return true;
							}
							
							try {
								speed = Double.parseDouble(args[5]);
							} catch (Exception e) {
								player.sendMessage("Speed must be a number greater than or eaqual to 0");
								return true;
							}
							
							emiterModifier.setTravelParticles(potentialGun, passParticle, spread, count, speed);
							player.sendMessage("Set travel particles");
							return true;
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit ParticlesTravel [Particle | Spread | Count | Speed]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("ParticlesHit")) {
						if (args.length == 6) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create).");
								return true;
							}
							
							Particle passParticle = null;
							Double spread = null;
							Integer count = null;
							Double speed = null;
							
							try {
								passParticle = Particle.valueOf(args[2]);
							} catch (Exception e) {
								player.sendMessage("Particle must be a particle.");
								return true;
							}
							
							try {
								spread = Double.parseDouble(args[3]);
							} catch (Exception e) {
								player.sendMessage("Spread must be a number greater than 0.");
								return true;
							}

							try {
								count = Integer.parseInt(args[4]);
							} catch (Exception e) {
								player.sendMessage("Count must be an integer greater than or eaqual to 0;");
								return true;
							}
							
							try {
								speed = Double.parseDouble(args[5]);
							} catch (Exception e) {
								player.sendMessage("Speed must be a number greater than or eaqual to 0;");
								return true;
							}
							
							emiterModifier.setHitParticles(potentialGun, passParticle, spread, count, speed);
							player.sendMessage("Set hit particles");
							return true;
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit ParticlesHit [Particle | Spread | Count | Speed]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("ParticlesShoot")) {
						if (args.length == 6) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create).");
								return true;
							}
							
							Particle passParticle = null;
							Double spread = null;
							Integer count = null;
							Double speed = null;
							
							try {
								passParticle = Particle.valueOf(args[2]);
							} catch (Exception e) {
								player.sendMessage("Particle must be a particle.");
								return true;
							}
							
							try {
								spread = Double.parseDouble(args[3]);
							} catch (Exception e) {
								player.sendMessage("Spread must be a number greater than 0");
								return true;
							}

							try {
								count = Integer.parseInt(args[4]);
							} catch (Exception e) {
								player.sendMessage("Count must be an integer greater than or eaqual to 0");
								return true;
							}
							
							try {
								speed = Double.parseDouble(args[5]);
							} catch (Exception e) {
								player.sendMessage("Speed must be a number greater than or eaqual to 0");
								return true;
							}
							
							emiterModifier.setShootParticles(potentialGun, passParticle, spread, count, speed);
							player.sendMessage("Set shoot particles");
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit ParticlesShoot [Particle | Spread | Count | Speed]");
						}
					} else if (args[1].equalsIgnoreCase("Cooldown")) {
						if (args.length == 3) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							try {
								emiterModifier.setBulletCooldown(potentialGun, Double.parseDouble(args[2]));
								player.sendMessage("Set bullet cooldown");
								return true;
							}catch (Exception e) {
								player.sendMessage("Cooldown must be a number greater than or eaqual to 0");
								return true;
							}
							
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit ShootCooldown [Cooldown]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("Distance")) {
						if (args.length == 3) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							try {
								emiterModifier.setBulletTravelDistance(potentialGun, Double.parseDouble(args[2]));
								player.sendMessage("Set bullet travel distance");
								return true;
							}catch (Exception e) {
								player.sendMessage("Distance must be a number greater than or eaqual to 0");
								return true;
							}
							
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit TravelDistance [Distance]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("Reflection")) {
						if (args.length == 4) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							double probability;
							double degrees;
							
							try {
								probability = Double.parseDouble(args[2]);
								if (probability > 1 | probability < 0) {
									player.sendMessage("Probability must be a number between 0 and 1");
									return true;
								}
							} catch (Exception e) {
								player.sendMessage("Probability must be a number between 0 and 1");
								return true;
							}
							
							try {
								degrees = Double.parseDouble(args[3]);
								if (degrees > 90 | degrees < 0) {
									player.sendMessage("Angle must be a number between 0 and 90");
									return true;
								}
							} catch (Exception e) {
								player.sendMessage("Angle must be a number between 0 and 90");
								return true;
							}
							
							emiterModifier.setBulletReflectionInfo(potentialGun, probability, degrees);
							player.sendMessage("Set bullet reflection data");
							return true;
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit Reflection [Probability | Angle]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("Damage")) {
						if (args.length == 3) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							try {
								int playerDamage = Integer.parseInt(args[2]);
								if (playerDamage < 0) {
									player.sendMessage("Damage must be an integer greater than or eaqual to 0");
									return true;
								}
								emiterModifier.setBulletDamage(potentialGun, playerDamage);
								player.sendMessage("Set bullet damage to " + playerDamage);
								return true;
							}catch (Exception e) {
								player.sendMessage("Damage must be an integer greater than or eaqual to 0");
								return true;
							}
							
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit Damage [Damage]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("ClipSize")) {
						if (args.length == 3) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							try {
								int clipSize = Integer.parseInt(args[2]);
								if (clipSize < 0) {
									player.sendMessage("Clip size must be an integer greater than or eaqual to 0");
									return true;
								}
								emiterModifier.setBulletDamage(potentialGun, clipSize);
								player.sendMessage("Set clip size to " + clipSize);
								return true;
							}catch (Exception e) {
								player.sendMessage("Clip size must be an integer greater than or eaqual to 0");
								return true;
							}
							
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit ClipSize [ClipSize]");
							return true;
						}
					} else if (args[1].equalsIgnoreCase("Automatic")) {
						if (args.length == 4) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							boolean isAutomatic;
							double startUpTime;
							
							try {
								isAutomatic = Boolean.parseBoolean(args[2]);
							} catch (Exception e) {
								player.sendMessage("IsAutomatic must be either True or False");
								return true;
							}
							
							try {
								startUpTime = Double.parseDouble(args[3]);
								if (startUpTime < 0) {
									player.sendMessage("Start up time must be a number greater than 0");
									return true;
								}
							} catch (Exception e) {
								player.sendMessage("Start up time must be a number greater than 0");
								return true;
							}
							
							emiterModifier.setAutomatic(potentialGun, isAutomatic, startUpTime);
							player.sendMessage("Set automatic data");
							return true;
							
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit Automatic [IsAutomatic | StartUpTime]");
						}
					} else if (args[1].equalsIgnoreCase("Crit")) {
						if (args.length == 4) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							double critChance;
							double multiplier;
							
							try {
								critChance = Double.parseDouble(args[2]);
								if (critChance < 0 | critChance > 1) {
									player.sendMessage("Crit Chance must be a number between 0 and 1");
									return true;
								}
							} catch (Exception e) {
								player.sendMessage("Crit Chance must be a number between 0 and 1");
								return true;
							}
							
							try {
								multiplier = Double.parseDouble(args[3]);
							} catch (Exception e) {
								player.sendMessage("Multiplier must be a number");
								return true;
							}
							
							emiterModifier.setCritData(potentialGun, critChance, multiplier);
							player.sendMessage("Set crit data");
							return true;
							
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit Crit [CritChance | DamageMultiplier]");
						}
					} else if (args[1].equalsIgnoreCase("Pierce")) {
						if (args.length == 3) {
							
							ItemStack potentialGun = player.getItemInHand();
							
							if (!gunMain.doesThisGunExist(potentialGun)) {
								player.sendMessage("You must be holding a gun (Create a gun by holding an item and doing /gun Create)");
								return true;
							}
							
							int pierces;
							
							try {
								pierces = Integer.parseInt(args[2]);
								if (pierces < 0) {
									player.sendMessage("Pierces must be an integer greater than 0");
									return true;
								}
							} catch (Exception e) {
								player.sendMessage("Pierces must be an integer greater than 0");
								return true;
							}
							
							emiterModifier.setPierce(potentialGun, pierces);
							player.sendMessage("Set pierces");
							return true;
							
							
						} else {
							player.sendMessage("Proper Syntax: /gun Edit Pierces [Pierces]");
						}
					} else {
						player.sendMessage("Proper Syntax: /gun Edit [Property]");
					}
				} else {
					player.sendMessage("Proper Syntax: /gun Edit [Property]");
				}
			}
			
			if(args[0].equalsIgnoreCase("BULLETCONSISTENCY")) {
				if (args.length == 2) {
					int bulletConsistency = 0;
					
					try {
						bulletConsistency = Integer.parseInt(args[1]);						
						if (bulletConsistency > 0) {						
							
							emiterModifier.setBulletConsistency(bulletConsistency);
							player.sendMessage("Set bullet consistency to " + bulletConsistency);
							
						} else {
							player.sendMessage("Bullet Consistency must be a number greater than 0");	
						}
					} catch(NumberFormatException e) {
						player.sendMessage("Bullet Consistency must be a number");
					}
				} else {
					player.sendMessage("Proper Syntax: /gun BulletConsistency [Number]");					
				}
			}
		}
		
		return true;
	}
}