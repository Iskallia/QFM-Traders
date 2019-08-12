package kaptainwutax.traders.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import kaptainwutax.traders.Trade;
import net.minecraft.init.Items;

public class ConfigTrades extends Config {
	
	@Expose public List<Trade> TRADES = new ArrayList<Trade>();
	@Expose public List<String> BLACKLIST = new ArrayList<String>();

	public ConfigTrades() {
		//TRADES.add(new Trade(Items.APPLE, 10, 5));
		//BLACKLIST.add(Items.AIR.getRegistryName().toString());
	}
	
	@Override
	public String getLocation() {
		return "trades.json";
	}
	
}
