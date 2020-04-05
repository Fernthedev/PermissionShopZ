package nl.tabuu.permissionshopz;


import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.config.snakeyaml.SnakeYamlConfig;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.permission.Permission;
import nl.tabuu.permissionshopz.bstats.Metrics;
import nl.tabuu.permissionshopz.command.PermissionShopCommand;
import nl.tabuu.permissionshopz.data.PerkManager;
import nl.tabuu.permissionshopz.permissionhandler.IPermissionHandler;
import nl.tabuu.permissionshopz.permissionhandler.PermissionHandler_CUSTOM;
import nl.tabuu.permissionshopz.permissionhandler.PermissionHandler_LuckPerms;
import nl.tabuu.permissionshopz.permissionhandler.PermissionHandler_VAULT;
import nl.tabuu.tabuucore.configuration.IConfiguration;
import nl.tabuu.tabuucore.plugin.TabuuCorePlugin;
import nl.tabuu.tabuucore.util.Dictionary;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

public class PermissionShopZ extends TabuuCorePlugin {
	private static PermissionShopZ INSTANCE;

	@Getter
	private static Config<PerkManager> config;

	private File configFile;


	private Dictionary _local;
	private PerkManager _manager;
	private IConfiguration _config;
	private IPermissionHandler _permissionHandler;
	private static Permission perms = null;


	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	@Override
	public void onEnable() {
		INSTANCE = this;

		_config = getConfigurationManager().addConfiguration("config");
		_local = getConfigurationManager().addConfiguration("lang").getDictionary("");

		_manager = new PerkManager();
		configFile = new File(this.getDataFolder(), "shop_data.yml");
		try {
			config = new SnakeYamlConfig<>(_manager, configFile);
		} catch (ConfigLoadException e) {
			e.printStackTrace();
		}
		load(config);

		setupPermissionHandler();

		this.getCommand("permissionshopz").setExecutor(new PermissionShopCommand());
		new Metrics(this);
		this.getLogger().info("PermissionShopZ is now enabled.");
	}

	private void setupPermissionHandler(){
		switch (_config.getString("PermissionManager").toUpperCase()){

			case "VAULT":
				if(this.getServer().getPluginManager().getPlugin("Vault") != null) {
					if(!setupPermissions()) {
						getLogger().severe("Vault permissions not configured");
						this.getServer().getPluginManager().disablePlugin(this);
					} else {
						_permissionHandler = new PermissionHandler_VAULT();
					}
				}
				else{
					this.getLogger().severe("Vault was not found! Please edit the config.yml");
					this.getServer().getPluginManager().disablePlugin(this);
				}
				break;

			case "LUCK_PERMS":
				RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
				if(provider != null){
					_permissionHandler = new PermissionHandler_LuckPerms();
				}
				else{
					this.getLogger().severe("LuckPerms provider was not found! Please edit the config.yml");
					this.getServer().getPluginManager().disablePlugin(this);
				}
				break;

			case "CUSTOM":
				_permissionHandler = new PermissionHandler_CUSTOM();
				break;

			default:
				this.getLogger().severe("No valid permission manager found! Please edit the config.yml");
				this.getServer().getPluginManager().disablePlugin(this);
				break;
		}
	}

	@Override
	public void onDisable() {
		try {
			save(config);
		} catch (ConfigLoadException e) {
			e.printStackTrace();
		}

		this.getLogger().info("PermissionShopZ is now disabled.");
	}

	public static void save(Config<PerkManager> config) throws ConfigLoadException {
		config.syncSave();
//		config.syncSave();
//		try (FileOutputStream fileOutputStream = new FileOutputStream(file);
//			 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
//
//			objectOutputStream.writeObject(_manager);
//		} catch (IOException exception){
//			exception.printStackTrace();
//			getLogger().severe("Could not save data!");
//		}
	}

	public static void load(Config<PerkManager> config){
		try {
			config.syncLoad();
		} catch (ConfigLoadException e) {
			e.printStackTrace();
		}
//		try (FileInputStream fileInputStream = new FileInputStream(file);
//			 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
//
//			_manager = (PerkManager) objectInputStream.readObject();
//		} catch (IOException | ClassNotFoundException exception) {
//			getLogger().warning("No data found!");
//		}
	}

	public void reload() throws ConfigLoadException {
		save(config);
		load(config);

		this.getConfigurationManager().reloadAll();
	}

	public Dictionary getLocal(){
		return _local;
	}

	public IPermissionHandler getPermissionHandler(){
		return _permissionHandler;
	}

	public PerkManager getPerkManager(){
		return _manager;
	}

	public static PermissionShopZ getInstance(){
		return INSTANCE;
	}

	public static Permission getPerms() {
		return perms;
	}
}
