package kaptainwutax.traders.init;

import java.util.HashMap;
import java.util.Map;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import kaptainwutax.traders.config.ConfigTrades;
import kaptainwutax.traders.util.Pair;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InitTrades {

	public static Map<Pair<Product, Product>, Trade> TRADES = new HashMap<Pair<Product, Product>, Trade>();
	
	public static void registryTrades(ConfigTrades config) {
		//Add the trades in config.
		for(Trade trade: config.TRADES) {
			Product sell = trade.getSell();
			Product buy = trade.getBuy();
			if(config.BLACKLIST.contains(sell) || config.BLACKLIST.contains(buy))continue;
			TRADES.put(trade.getKey(), trade);
		}
		
		//Add the rest of the items in the game, with default value.
		for(Item item: Item.REGISTRY) {
			NonNullList<ItemStack> items = NonNullList.create();
			item.getSubItems(item.getCreativeTab() != null ? item.getCreativeTab() : CreativeTabs.SEARCH, items);
			
			for(ItemStack metaItem: items) {
				Trade dummy = new Trade(metaItem.getItem(), metaItem.getMetadata());
				if(config.TRADES.contains(dummy))continue;
				
				Product sell = dummy.getSell();
				Product buy = dummy.getBuy();
				if(config.BLACKLIST.contains(sell) || config.BLACKLIST.contains(buy))continue;
				
				TRADES.put(dummy.getKey(), dummy);
			}
		}
	}
	
}
