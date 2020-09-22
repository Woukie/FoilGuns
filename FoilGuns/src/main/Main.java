package main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import foilGuns.FoilListener;
import inputs.Commands;
import inputs.CompleteTab;

public class Main extends JavaPlugin implements Listener{
	public static World world;
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		System.out.println(ChatColor.GREEN + "FoilGun has been enabled.");
		plugin = this;
		
		world = Bukkit.getWorld("world");
		
		this.getCommand("gun").setExecutor(new Commands());
		this.getCommand("gun").setTabCompleter(new CompleteTab());
		this.getServer().getPluginManager().registerEvents(new FoilListener(), this);
	}
	
	@Override
	public void onDisable() {
		System.out.println(ChatColor.GREEN + "FoilGun has been disabled.");
	}
}