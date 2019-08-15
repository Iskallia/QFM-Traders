package kaptainwutax.traders.config;

import kaptainwutax.traders.Trade;

public class ConfigBobby extends ConfigTrades {

	public ConfigBobby() {

	}

	@Override
	public String getLocation() {
		return "bobby.json";
	}

	@Override
	protected void resetConfig() {
		this.DEFAULT_TRADE = new Trade(null, null, null, 0);		
	}

}
