package me.titan.customcommands.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TitanConfig  {


	final JavaPlugin plugin;
	final File file;
	protected final YamlConfiguration config;

	String pathPrefix;
	public TitanConfig(String fileName, JavaPlugin plugin) {

		this.plugin = plugin;
		file = new File(plugin.getDataFolder(),fileName);
		if(!file.exists()){
			plugin.saveResource(fileName,false);
		}
		config = YamlConfiguration.loadConfiguration(file);


	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	public String getPathPrefix() {
		if(pathPrefix == null) return "";
		return pathPrefix + ".";
	}

	public Set<String> singleLayerKeySet(String path){
		return Objects.requireNonNull(config.getConfigurationSection(getPathPrefix() + path)).getKeys(false);
	}
	public Object get(String path){
		return config.get(getPathPrefix() + path);
	}
	public String getString(String path){
		return config.getString(getPathPrefix() + path);
	}
	public String getString(String path, String def){
		if(!config.contains(getPathPrefix() + path)) return def;
		return config.getString(getPathPrefix() + path);
	}
	public List<String> getStringList(String path){
		return config.getStringList(getPathPrefix() + path);

	}
	public int getInt(String path){
		return config.getInt(getPathPrefix() + path);
	}
	public void set(String path, Object ob){
		config.set(getPathPrefix() + path,ob);
		save();
	}
	public void setNoSave(String path, Object ob){
		config.set(getPathPrefix() + path,ob);
	}
	public void remove(String path){
		set(path,null);
	}
	public void forceReload(){
		if(!file.exists()){
			plugin.saveResource(file.getName(),false);
		}
		try {
			config.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	public boolean getBoolean(String path){
		return config.getBoolean(getPathPrefix() + path);
	}
	public boolean contains(String path){
		return config.contains(getPathPrefix() + path);
	}
	public Object getOrSetDefault(String path, Object value){
		if(!config.contains(getPathPrefix() + path)){
			set(path,value);
		}
		return config.get(getPathPrefix() + path);
	}

	public void save(){
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void init() {
	}
}
