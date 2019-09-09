package com.enteente.plotborder.utils;

import com.enteente.plotborder.Main;
import com.enteente.plotborder.handler.CommandHandler;
import com.enteente.plotborder.utils.Blockutils;
import com.enteente.plotborder.utils.BorderEntry;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

import com.github.intellectualsites.plotsquared.plot.object.BlockBucket;
import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import com.github.intellectualsites.plotsquared.plot.util.MathMan;
import com.github.intellectualsites.plotsquared.plot.util.block.GlobalBlockQueue;


public class PlotUtils {
	public static boolean UpdatePlot(Player player, String component, String value) {
		
		PlotPlayer pp=PlotPlayer.wrap(player);
        BorderEntry be=null;
        
        switch(component) {
        case "border":
        	be=Main.getInstance().getBorder(value);
        	break;
        case "wall":
        	be=Main.getInstance().getWall(value);
        	break;
        	
        }
        
        if(be==null) {
        	player.sendMessage("Ungültiger Eintrag: "+value);
        	return false;
        }
		
        if(!be.checkPermissions(player)) {
        	player.sendMessage("Nicht genug rechte für "+be.getName());
        	return false;
        }
        
        String mat=be.getMaterial();
        
        
        BlockBucket plotBlocks=null;
        try {
            plotBlocks = Blockutils.parseString(mat);
            player.sendMessage("Erstelle "+be.getName());
        } catch (Exception e) 
        { 
            e.printStackTrace(); 
            player.sendMessage(e.toString());
            System.out.println(e); 
        }
        
        if(plotBlocks==null || plotBlocks.isEmpty()) {
        	player.sendMessage("Ungültiger Block!");
        	return false;
        }
        
        Location location = CommandHandler.getLocationFull(player);
        final Plot plot = location.getPlotAbs();
        UUID uuid = pp.getUUID();
        
        if (plot == null) {
        	player.sendMessage("Du bist nicht auf einem Plot!");
        	return false;
        }
        if (!player.hasPermission("plots.admin")  && (!plot.hasOwner() || !plot.isOwner(uuid))) {
            player.sendMessage("Das ist nicht dein Plot!");
            return false;
        }            
        if (plot.getRunning() > 0) {
        	player.sendMessage("Eine andere Aktion wird gerade ausgeführt. Bitte warte, bis diese beendet ist.");
            return false;
        }
        plot.addRunning();
        
        player.sendMessage("Erzeuge Rand");
        try {
            for (Plot current : plot.getConnectedPlots()) {
                current.setComponent(component, plotBlocks);
            }            
        } catch (Exception e) 
	    { 
	        player.sendMessage("Ein Fehler ist aufgetreten");
	    }
        
        GlobalBlockQueue.IMP.addTask(plot::removeRunning);        
        
		
		return true;
	}
	
    public static Location getLocationFull(final Entity entity) {
        final org.bukkit.Location location = entity.getLocation();
        return new Location(location.getWorld().getName(), MathMan.roundInt(location.getX()),
            MathMan.roundInt(location.getY()), MathMan.roundInt(location.getZ()), location.getYaw(),
            location.getPitch());
    }

}
