package kaptainwutax.traders.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.util.Product;
import kaptainwutax.traders.util.Trade;
import net.minecraft.init.Items;

public class ConfigTom extends ConfigTrades {

	@Expose public List<String> POI_LOSS_PHRASES = new ArrayList<String>();
	@Expose public List<String> DESPAWN_PHRASES = new ArrayList<String>();
	
	@Expose public int DESPAWN_DELAY;
	
	@Expose public int TRADES_COUNT;

	public ConfigTom() {

	}

	@Override
	public String getLocation() {
		return "tom.json";
	}
	
	@Override
	protected void resetConfig() {
		this.POI_LOSS_PHRASES.add("I'm lost, where should I go?");
		this.DESPAWN_PHRASES.add("Nothing left for me here...");
		this.DESPAWN_DELAY = 24000;
		this.TRADES_COUNT = 10;
		this.DEFAULT_TRADE = new Trade(new Product(null, 0, 10, null), null, new Product(Items.DIAMOND, 0, 1, null), 5000);		
	}

}
