package kaptainwutax.traders.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.Trade;
import kaptainwutax.traders.config.ConfigTrades;
import net.minecraft.item.Item;

public class InitTrades {

	public static Map<Item, Trade> TRADES = new HashMap<Item, Trade>();
	
	public static void registryTrades(ConfigTrades config) {
		//Add the trades in config.
		for(Trade trade: config.TRADES) {
			TRADES.put(trade.getProduct(), trade);
		}
		
		//Add the rest of the items in the game, with default value.
		for(Item item: Item.REGISTRY) {
			Trade dummy = new Trade(item);			
			if(config.TRADES.contains(dummy))continue;
			TRADES.put(item, dummy);
		}
		
		//Removes all trades with items in the blacklist.
		for(String name: config.BLACKLIST) {
			TRADES.remove(Item.getByNameOrId(name));
		}
	}
	
}
