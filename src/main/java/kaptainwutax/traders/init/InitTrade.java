package kaptainwutax.traders.init;

import java.util.HashMap;
import java.util.Map;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import kaptainwutax.traders.config.ConfigTrades;
import kaptainwutax.traders.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InitTrade {

	public static Map<Pair<Product, Product>, Trade> TOM = new HashMap<Pair<Product, Product>, Trade>();
	public static Map<Pair<Product, Product>, Trade> BOBBY = new HashMap<Pair<Product, Product>, Trade>();
	
	public static void registryTrades() {
		registryTrade(TOM, InitConfig.CONFIG_TOM);
		registryTrade(BOBBY, InitConfig.CONFIG_BOBBY);
	}
	
	public static void registryTrade(Map<Pair<Product, Product>, Trade> trades, ConfigTrades config) {
		//Add the trades in config.
		for(Trade trade: config.TRADES) {
			Product sell = trade.getSell();
			Product buy = trade.getBuy();
			if(config.BLACKLIST.contains(sell))continue;
			trades.put(trade.getKey(), trade);
		}
		
		if(config.DEFAULT.getSell().getAmount() <= 0 || config.DEFAULT.getBuy().getAmount() <= 0)return;
		
		//Add the rest of the items in the game, with default value.
		for(Item item: Item.REGISTRY) {
			if(item.getCreativeTab() == null)continue;
			NonNullList<ItemStack> items = NonNullList.create();
			
			item.getSubItems(item.getCreativeTab(), items);
			
			for(ItemStack metaItem: items) {
				Trade dummy = new Trade(metaItem.getItem(), metaItem.getMetadata(), config.DEFAULT);
				if(config.TRADES.contains(dummy))continue;
				
				Product sell = dummy.getSell();
				Product buy = dummy.getBuy();
				if(config.BLACKLIST.contains(sell))continue;
				
				trades.put(dummy.getKey(), dummy);
			}
		}
	}
	
}
