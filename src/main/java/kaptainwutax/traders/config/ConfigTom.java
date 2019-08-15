package kaptainwutax.traders.config;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

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
