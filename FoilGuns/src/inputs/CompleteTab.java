package inputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CompleteTab implements TabCompleter {
	String[] firstArgument = {"Create", "Delete", "Edit", "BulletConsistency", "List"};
	String[] editArguments = {"ParticlesTravel", "ParticlesHit", "ParticlesShoot", "Cooldown", "Distance", "Reflection", "Damage", "ClipSize", "Automatic", "Crit", "Pierce", "Offset"};
	String[] listArguments = {"Holding", "All"};
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return getTabResponse(firstArgument, args[0]);
		}
		
		if (args.length == 2 && args[0].equalsIgnoreCase("Edit")) {
			return getTabResponse(editArguments, args[1]);
		}
		
		if (args.length == 2 && args[0].equalsIgnoreCase("List")) {
			return getTabResponse(listArguments, args[1]);
		}
		
		//third argument territory
		
		if (args.length == 3 && args[1].equalsIgnoreCase("ParticlesTravel") | args[1].equalsIgnoreCase("ParticlesShoot") | args[1].equalsIgnoreCase("ParticlesHit")) {
			String[] allParticlesArray = new String[Particle.values().length];
			List<Particle> allParticlesList = new ArrayList<Particle>();
			
			for (Particle particle : Particle.values()) {
				allParticlesList.add(particle);
			}
			
			for (int i = 0; i < allParticlesArray.length; i++) {
				allParticlesArray[i] = allParticlesList.get(i).toString();
			}
			
			return getTabResponse(allParticlesArray, args[2]);
		}
		
		return null;
	}
	
	private List<String> getTabResponse(String[] possibleArguments, String playerArgument) {
		List<String> returnList = new ArrayList<String>();
		returnList.addAll(Arrays.asList(possibleArguments));
		
		returnList.removeIf(n -> !n.toLowerCase().startsWith(playerArgument.toLowerCase()));
		return returnList;
	}
}