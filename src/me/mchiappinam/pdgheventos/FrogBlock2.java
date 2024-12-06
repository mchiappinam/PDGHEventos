package me.mchiappinam.pdgheventos;

import org.bukkit.Location;
import org.bukkit.Material;

public class FrogBlock2 {
	private Location l;
	private Material m;
	private byte d;
	
	public FrogBlock2(Location l2, Material m2, byte d2) {
		l=l2;
		m=m2;
		d=d2;
	}
	
	public Location getLocation() {
		return l;
	}
	
	public Material toMaterial() {
		return m;
	}
	
	public byte toData() {
		return d;
	}
}
