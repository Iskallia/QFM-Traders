package kaptainwutax.traders.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import net.minecraft.init.Items;

public class ConfigTrades extends Config {
	
	@Expose public List<Trade> TRADES = new ArrayList<Trade>();
	@Expose public List<Product> BLACKLIST = new ArrayList<Product>();

	public ConfigTrades() {
		TRADES.add(new Trade(Items.AIR, 0));
		TRADES.add(new Trade(new Product(Items.APPLE, 0, 1), new Product(Items.BED, 2, 10), 5, true));
		TRADES.add(new Trade(new Product(Items.ARROW, 0, 3), new Product(Items.BEEF, 0, 12), 5, false));
		TRADES.add(new Trade(new Product(Items.BEEF, 0, 40), new Product(Items.ARROW, 0, 13), 5, false));
		BLACKLIST.add(new Product(Items.AIR, 0));
	}
	
	@Override
	public String getLocation() {
		return "trades.json";
	}
	
}
