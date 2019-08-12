package kaptainwutax.traders.entity;

import kaptainwutax.traders.Trade;
import kaptainwutax.traders.init.InitTrades;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityTrader extends EntityVillager {

	public EntityTrader(World world) {
		super(world);	
		
		MerchantRecipeList trades = new MerchantRecipeList();
		trades.clear();
		
		for(Trade trade: InitTrades.TRADES.values()) {
			//if(trade.getValue() < 2)continue;
			ItemStack buy = new ItemStack(trade.getProduct(), trade.getValue());
			ItemStack sell = new ItemStack(Items.DIAMOND, 1);
			
			MerchantRecipe trade1 = new MerchantRecipe(buy, sell);
			MerchantRecipe trade2 = new MerchantRecipe(sell, buy);
			trades.add(trade1);
			trades.add(trade2);
			//System.out.println("Added " + trade1.getItemToBuy().getItem().getRegistryName().getResourceDomain());
		}
		
		this.getRecipes(null).addAll(trades);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		

	}

}
