package kaptainwutax.traders.config;

import kaptainwutax.traders.util.Product;
import kaptainwutax.traders.util.Trade;
import net.minecraft.init.Items;

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
		this.CUSTOM_TRADES.add(new Trade(new Product(Items.APPLE, 0, 8), null, new Product(Items.GOLDEN_APPLE, 0, 1), 5));
	}

}
