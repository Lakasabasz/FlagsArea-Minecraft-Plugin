package pl.lakasabasz.mc.flagsareas;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "FlagsAreas", version = "0.0.3.1")
@Description(value = "Plugin skanujący serwer w poszukiwaniu bannerów")
@Author(value = "Łukasz Łakasabasz Mastalerz")
@LogPrefix("FlagsAreas")
@Commands(@org.bukkit.plugin.java.annotation.command.Command(name = "flagsareas", desc = "Komaneda poszukująca sztandarów i tworząca raport", permission = "flagsareas.cmd", usage = "/flagsareas"))
@Permission(name = "flagsareas.cmd")
@Permission(name = "flagsareas.*", children = {@ChildPermission(name="flagsareas.cmd")})
@ApiVersion(ApiVersion.Target.v1_16)
public class Main extends JavaPlugin {
	private static Main instance;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		instance = this;
		Bukkit.getConsoleSender().sendMessage("Działa na wersji " + this.getDescription().getVersion().toString());
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("Spanko na wersji " + this.getDescription().getVersion().toString());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Report r = new Report();
		r.scanWorld();
		return true;
	}

	public static org.bukkit.plugin.Plugin getInstance() {
		return instance;
	}

}
