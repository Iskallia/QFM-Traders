package kaptainwutax.traders.config;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class ConfigTom extends ConfigTrades {

	public ConfigTom() {
		super(new Trade(new Product(Items.AIR, 0, 10), new Product(Items.DIAMOND, 0, 1), 5000, false));
		
		TRADES.add(new Trade(new Product(Items.APPLE, 0, 64), new Product(Items.GOLDEN_APPLE, 1, 1), 2, false));
		
		for(Item item: Item.REGISTRY) {
			if(item.getItemStackLimit() == 1) {				
				BLACKLIST.add(new Product(item, item.getHasSubtypes() ? -1 : 0));
			}
		}
		
		BLACKLIST.add(new Product(Items.SPAWN_EGG, -1));
		BLACKLIST.add(new Product(Items.TIPPED_ARROW, -1));
		BLACKLIST.add(new Product(Items.SPECTRAL_ARROW, -1));
	}

	@Override
	public String getLocation() {
		return "tom.json";
	}

}
