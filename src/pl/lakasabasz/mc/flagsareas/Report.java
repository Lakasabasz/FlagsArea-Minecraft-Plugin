package pl.lakasabasz.mc.flagsareas;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public class Report implements Runnable {
	private static Map<List<Pattern>, List<Location>> bannerCounter;
	private int x = -2000;
	private int maxX = 2000;
	private int minZ = -2000;
	private int maxZ = 2000;
	private int minY = 10;
	private int maxY = 210;
	private World w;
	private int logInterval = 100;
	private int taskInterval = 2;
	private BukkitTask tasktimer;
	
	public Report() {
		bannerCounter = new HashMap<List<Pattern>, List<Location>>();
		this.x = -2000;
		this.maxX = 2000;
		this.minY = 10;
		this.maxY = 210;
		this.minZ = -2000;
		this.maxZ = 2000;
		this.taskInterval = 2;
		this.logInterval = 100;
		this.w = Bukkit.getWorld("world");
	}
	
	public Report(int minX, int maxX, int minZ, int maxZ, int minY, int maxY, World w, int taskInterval, int logInterval) {
		bannerCounter = new HashMap<List<Pattern>, List<Location>>();
		this.x = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
		this.taskInterval = taskInterval;
		this.logInterval = logInterval;
		this.w = w;
	}
	
	public boolean saveToFile(String s) {
		FileWriter f;
		try {
			f = new FileWriter(Main.getInstance().getDataFolder().getAbsolutePath() + "/" + s);
			for(List<Pattern> banner : bannerCounter.keySet()) {
				String bannerName = new String();
				for(Pattern p : banner) {
					bannerName += p.getColor().name() + " " + p.getPattern().name() + "\t";
				}
				bannerName += "\n";
				f.write(bannerName);
				String bannerPositions = new String();
				for(Location loc : bannerCounter.get(banner)) {
					bannerPositions += "\t" + loc.toVector().toString() + "\n";
				}
				f.write(bannerPositions);
			}
			f.close();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	public void scanWorld() {
		if(tasktimer != null) tasktimer = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 0, taskInterval);
	}

	@Override
	public void run() {
		for(int y = minY; y < maxY; y++) {
			for(int z = minZ; z < maxZ; z++) {
				BlockState b = w.getBlockAt(x, y, z).getState(true);
				if(b instanceof Banner) {
					if(!bannerCounter.containsKey(((Banner) b).getPatterns())) {
						bannerCounter.put(((Banner) b).getPatterns(), new ArrayList<Location>());
					}
					bannerCounter.get(((Banner) b).getPatterns()).add(new Location(w, x, y, z));
				}
			}
		}
		x++;
		if(x % logInterval == 0) Bukkit.getConsoleSender().sendMessage("[INFO] " + x + " /2000");
		if(x >= maxX) {
			if(!this.saveToFile("bannerlog.log")) {
				Bukkit.getConsoleSender().sendMessage("[FlagsAreas][ERROR] " + ChatColor.RED + "Saving to file failed");
			}
			tasktimer.cancel();
		}
	}
}
