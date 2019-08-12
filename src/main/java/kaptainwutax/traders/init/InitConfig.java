package kaptainwutax.traders.init;

import java.io.FileNotFoundException;

import kaptainwutax.traders.config.Config;
import kaptainwutax.traders.config.ConfigTrades;

public class InitConfig {

	public static ConfigTrades CONFIG_TRADES = new ConfigTrades();
	
	public static void registerConfigs() {
		registerConfig(CONFIG_TRADES);
	}
	
	public static void registerConfig(Config config) {
		try {config = config.readConfig();} 
		catch (FileNotFoundException e) {config.generateConfig();}	
	}
	
}
