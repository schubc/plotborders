package com.enteente.plotborder.handler;

import com.enteente.plotborder.Main;
import com.enteente.plotborder.utils.Blockutils;
import com.enteente.plotborder.utils.BorderEntry;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Entity;

import com.github.intellectualsites.plotsquared.plot.PlotSquared;
import com.github.intellectualsites.plotsquared.plot.config.Captions;
import com.github.intellectualsites.plotsquared.plot.object.BlockBucket;
import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotArea;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import com.github.intellectualsites.plotsquared.plot.object.RegionWrapper;
import com.github.intellectualsites.plotsquared.plot.util.MathMan;
import com.github.intellectualsites.plotsquared.plot.util.UUIDHandler;
import com.github.intellectualsites.plotsquared.plot.util.block.GlobalBlockQueue;
import com.github.intellectualsites.plotsquared.plot.config.Configuration;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotBlock;

import com.github.intellectualsites.plotsquared.api.PlotAPI;

public class CommandHandler implements CommandExecutor {


    public static Location getLocationFull(final Entity entity) {
        final org.bukkit.Location location = entity.getLocation();
        return new Location(location.getWorld().getName(), MathMan.roundInt(location.getX()),
            MathMan.roundInt(location.getY()), MathMan.roundInt(location.getZ()), location.getYaw(),
            location.getPitch());
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player) {
        	
        	
            Player player = (Player) commandSender;
            PlotPlayer pp=PlotPlayer.wrap(player);

            
        	if(args.length == 0) {
        		player.sendMessage("Benutzung: /pb <wert>");        		
        	}
            
            
            
            String mat=args[0];
            
            
            if(mat.equals("reload")) {

            	if (!player.hasPermission("pb.reload")) {
            		player.sendMessage("Keine Berechtigung!");
            		return false;
            	}
            	
            	player.sendMessage("RELOADING CONGIG");
            	
            	Main.getInstance().getBorderConfig().reload();
            	Main.getInstance().loadBorders();
            	return true;
            }

            
            player.sendMessage(mat);
            BorderEntry be=Main.getInstance().getBorder(mat);
            if(be==null) {
            	player.sendMessage("Ungültiger Eintrag: "+mat);
            	return false;
            }
            
            
            if(!be.checkPermissions(player)) {
            	player.sendMessage("Nicht genug rechte für "+be.getName());
            }
            
            mat=be.getMaterial();
            
            
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
            if (!plot.hasOwner() || !plot.isOwner(uuid)) {
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
                    current.setComponent("border", plotBlocks);
                }            
            } catch (Exception e) 
		    { 
		        player.sendMessage("Ein Fehler ist aufgetreten");
		    }
            
            GlobalBlockQueue.IMP.addTask(plot::removeRunning);
            
            
        }
        return true;
    }
}
        
