package kaptainwutax.traders.config;

import kaptainwutax.traders.util.Product;
import kaptainwutax.traders.util.Trade;
import net.minecraft.init.Items;

public class ConfigTom extends ConfigTrades {

	public ConfigTom() {

	}

	@Override
	public String getLocation() {
		return "tom.json";
	}
	
	@Override
	protected void resetConfig() {
		this.DEFAULT_TRADE = new Trade(new Product(null, 0, 10), null, new Product(Items.DIAMOND, 0, 1), 5000);		
	}

}
