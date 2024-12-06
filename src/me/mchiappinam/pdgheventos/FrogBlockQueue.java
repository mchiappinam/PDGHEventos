package me.mchiappinam.pdgheventos;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class FrogBlockQueue extends Thread {
	private static BukkitTask timer = null;
	public static Queue<FrogBlock2> blocos = new LinkedList<FrogBlock2>();
	
	public FrogBlockQueue(List<FrogBlock2> l) {
		if(l!=null)
			blocos.addAll(l);	
	}
	public FrogBlockQueue(FrogBlock2 b) {
		if(b!=null)
			blocos.add(b);
	}
	
	public void run() {
		if(timer==null)
			timer();
	}
	
	private void timer() {
		timer = Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PDGHEventos"), new Runnable() {
			public void run() {
				for(int i=0;i<Main.bpt;i++) {
					FrogBlock2 b = blocos.poll();
					if(b!=null) {
						b.getLocation().getWorld().getBlockAt(b.getLocation()).setType(b.toMaterial());
						b.getLocation().getWorld().getBlockAt(b.getLocation()).setData(b.toData());
					}
				}
				if(blocos.size()==0)
					timer=null;
				else
					timer();
			}
		},1L);
	}
}
