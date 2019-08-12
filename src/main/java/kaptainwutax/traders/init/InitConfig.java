package kaptainwutax.traders.init;

import kaptainwutax.traders.config.ConfigTrades;

public class InitConfig {

	public static ConfigTrades CONFIG_TRADES = null;
	
	public static void registerConfigs() {
		CONFIG_TRADES = (ConfigTrades)new ConfigTrades().readConfig();
	}
	
}
