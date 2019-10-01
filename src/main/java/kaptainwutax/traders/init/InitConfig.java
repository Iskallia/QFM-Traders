package kaptainwutax.traders.init;

import kaptainwutax.traders.config.ConfigBobby;
import kaptainwutax.traders.config.ConfigTom;
import kaptainwutax.traders.config.ConfigTomNeeds;

public class InitConfig {

	public static ConfigTom CONFIG_TOM = null;
	public static ConfigBobby CONFIG_BOBBY = null;
	public static ConfigTomNeeds CONFIG_TOM_NEEDS = null;
	
	public static void registerConfigs() {
		CONFIG_TOM = (ConfigTom)new ConfigTom().readConfig();
		CONFIG_BOBBY = (ConfigBobby)new ConfigBobby().readConfig();
		CONFIG_TOM_NEEDS = (ConfigTomNeeds)new ConfigTomNeeds().readConfig();
	}
	
}
