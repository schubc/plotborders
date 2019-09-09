package com.enteente.plotborder.handler;

import com.enteente.plotborder.Main;
import com.enteente.plotborder.utils.PlotUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.util.MathMan;

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

            
            if(args.length == 0) {
        		player.sendMessage("Benutzung: /pb <border|wall|reload> <wert>");
        		return true;
        	}
            
            
            
            String component=args[0];
            
            
            if(component.equals("reload")) {

            	if (!player.hasPermission("pb.reload")) {
            		player.sendMessage("Keine Berechtigung!");
            		return false;
            	}
            	
            	player.sendMessage("RELOADING CONGIG");
            	
            	Main.getInstance().getBorderConfig().reload();
            	Main.getInstance().getWallConfig().reload();
            	Main.getInstance().loadBordersAndWalls();
            	return true;
            }

            if(component.equals("border") || component.equals("wall")) {
            	if(args.length >= 2) {
            		String value=args[1];
            		return PlotUtils.UpdatePlot(player, component, value);
            	}
            }

            
            
    		player.sendMessage("Benutzung: /pb <border|wall|reload> <wert>");
        }
        return true;
    }
}
        
