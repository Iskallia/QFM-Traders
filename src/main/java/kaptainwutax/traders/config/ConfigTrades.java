package kaptainwutax.traders.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;

public abstract class ConfigTrades extends Config {
	
	@Expose public Trade DEFAULT_TRADE = null;
	@Expose public List<Trade> CUSTOM_TRADES = new ArrayList<Trade>();
	@Expose public List<Product> BLACKLIST = new ArrayList<Product>();
	
	public ConfigTrades() {

	}
	
}
