package kaptainwutax.traders.config;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import net.minecraft.init.Items;

public class ConfigBobby extends ConfigTrades {

	public ConfigBobby() {
		super(new Trade(new Product(Items.AIR, 0, 0), new Product(Items.DIAMOND, 0, 0), 5000, false));
		
		TRADES.add(new Trade(new Product(Items.APPLE, 0, 8), new Product(Items.GOLDEN_APPLE, 0, 1), 2, false));
		TRADES.add(new Trade(new Product(Items.GOLDEN_APPLE, 0, 8), new Product(Items.GOLDEN_APPLE, 1, 1), 2, false));
	}

	@Override
	public String getLocation() {
		return "bobby.json";
	}

}
