package com.enteente.plotborder.utils;

import java.util.List;

import org.bukkit.entity.Player;

public class BorderEntry {
	private String id;
	private String name;
	private String material;
	private List<String> permissions;

	public BorderEntry(String id, String name, String material, List<String> permissions) {
		this.id=id;
		this.name=name;
		this.material=material;
		this.permissions=permissions;
	}
	
	public String getName() {
		return this.name;
	}
	public String getMaterial() {
		return this.material;
	}
	
	public boolean checkPermissions(Player player) {
		for (String p : this.permissions) {
			if(player.hasPermission("pb."+p)) {
				return true;
			}
		}
		return false;
	}
	
}
