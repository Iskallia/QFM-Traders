package kaptainwutax.traders.util;

import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;

public class CustomMerchantRecipe extends MerchantRecipe {

	private EntityTrader trader;

	public CustomMerchantRecipe(ItemStack buy, ItemStack extra, ItemStack sell, int maxUses, EntityTrader trader) {
		super(buy, extra == null ? ItemStack.EMPTY : extra, sell, 0, maxUses);		
		this.trader = trader;
	}
	
	public CustomMerchantRecipe(ItemStack buy, ItemStack extra, ItemStack sell, int maxUses, int currentUses, EntityTrader trader) {
		super(buy, extra == null ? ItemStack.EMPTY : extra, sell, currentUses, maxUses);		
		this.trader = trader;
	}
	
	public CustomMerchantRecipe(NBTTagCompound nbttagcompound) {
		super(nbttagcompound);
	}		
	
	@Override
	public void increaseMaxTradeUses(int increment) {
	    return;
	}
	
	@Override
	public ItemStack getItemToSell() {
		if(this.trader instanceof EntityTom && !this.trader.world.isRemote) {
			int happiness = ((EntityTom)this.trader).getHappiness();
			ItemStack discountedStack = super.getItemToSell().copy();
			int size = discountedStack.getCount() + happiness / 20;
			discountedStack.setCount(MathHelper.clamp(size, 0, discountedStack.getMaxStackSize()));	
			return discountedStack;
		}

		return super.getItemToSell();
	}
	
}
