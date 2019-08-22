package kaptainwutax.traders.config;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.util.Product;
import kaptainwutax.traders.util.Trade;
import net.minecraft.init.Items;

public class ConfigTom extends ConfigTrades {

	@Expose public int TRADES_COUNT;

	public ConfigTom() {

	}

	@Override
	public String getLocation() {
		return "tom.json";
	}
	
	@Override
	protected void resetConfig() {
		this.TRADES_COUNT = 10;
		this.DEFAULT_TRADE = new Trade(new Product(null, 0, 10, null), null, new Product(Items.DIAMOND, 0, 1, null), 5000);		
	}

}
