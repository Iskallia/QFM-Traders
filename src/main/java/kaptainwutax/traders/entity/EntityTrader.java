package kaptainwutax.traders.entity;

import kaptainwutax.traders.Trade;
import kaptainwutax.traders.init.InitTrades;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityTrader extends EntityVillager {

	public EntityTrader(World world) {
		super(world);
	}
	
	public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        
		MerchantRecipeList trades = this.getRecipes(null);
		trades.clear();
		
		for(Trade trade: InitTrades.TRADES.values()) {
			ItemStack buy = new ItemStack(trade.getProduct(), trade.getValue());
			ItemStack sell = new ItemStack(Items.DIAMOND, 1);
			trades.add(new MerchantRecipe(buy, sell));
			trades.add(new MerchantRecipe(sell, buy));
		}
    }
	
}
