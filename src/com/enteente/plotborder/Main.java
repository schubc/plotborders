package com.enteente.plotborder;

import java.util.HashMap;
import java.util.List;

import com.enteente.plotborder.handler.CommandHandler;
import com.enteente.plotborder.utils.BorderEntry;
import com.enteente.plotborder.utils.Configs;
import com.github.intellectualsites.plotsquared.bukkit.util.BukkitUtil;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main instance;
    private String packet = Bukkit.getServer().getClass().getPackage().getName();
    private String version = packet.substring(packet.lastIndexOf('.') + 1);	
	
    private Configs borderConfig;
    private Configs wallConfig;
    
    private HashMap<String, BorderEntry> borders = new HashMap<>();
    private HashMap<String, BorderEntry> walls = new HashMap<>();

    
	@Override
    public void onEnable() {
    	
		instance = this;
    	getCommand("plotborder").setExecutor(new CommandHandler());
    	
    	borderConfig=new Configs(this.getInstance(), "borders.yml", true);
    	wallConfig=new Configs(this.getInstance(), "walls.yml", true);
    	loadBordersAndWalls();
    }
    @Override
    public void onDisable() {
    }
    
    public BorderEntry getBorder(String id) {
    	if(this.borders.containsKey(id)) {
    		return this.borders.get(id);
    	}
    	return null;
    }

    public BorderEntry getWall(String id) {
    	if(this.walls.containsKey(id)) {
    		return this.walls.get(id);
    	}
    	return null;
    }    
    
    public Configs getBorderConfig() {
    	return borderConfig;
    }
    public Configs getWallConfig() {
    	return wallConfig;
    }
    
    public static Main getInstance() {
        return instance;
    }

    public String getVersion() {
        return version;
    }
    
    public void loadBordersAndWalls() {
    	if(!borders.isEmpty()) {
    		borders.clear();
    	}
    	for (String key : borderConfig.get().getConfigurationSection("").getKeys(false)) {
    		
    		String name=borderConfig.get().getString(key+".name");
    		String material=borderConfig.get().getString(key+".material");
    		List<String> permissions=(List<String>) borderConfig.get().getList(key+".groups");
    		
    		BorderEntry be=new BorderEntry(key, name, material, permissions);
    		borders.put(key, be);
    		
    	}
    	if(!walls.isEmpty()) {
    		walls.clear();
    	}
    	for (String key : wallConfig.get().getConfigurationSection("").getKeys(false)) {
    		
    		String name=wallConfig.get().getString(key+".name");
    		String material=wallConfig.get().getString(key+".material");
    		List<String> permissions=(List<String>) wallConfig.get().getList(key+".groups");
    		
    		BorderEntry be=new BorderEntry(key, name, material, permissions);
    		walls.put(key, be);
    		
    	}
    }
    
}
