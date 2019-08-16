package kaptainwutax.traders.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;

public class CustomMerchantRecipe extends MerchantRecipe {

	public CustomMerchantRecipe(ItemStack buy, ItemStack extra, ItemStack sell, int maxUses) {
		super(buy, extra == null ? ItemStack.EMPTY : extra, sell, 0, maxUses);		
	}
	
	public CustomMerchantRecipe(ItemStack buy, ItemStack extra, ItemStack sell, int maxUses, int currentUses) {
		super(buy, extra == null ? ItemStack.EMPTY : extra, sell, currentUses, maxUses);		
	}
	
	public CustomMerchantRecipe(NBTTagCompound nbttagcompound) {
		super(nbttagcompound);
	}		
	
	@Override
	public void increaseMaxTradeUses(int increment) {
	    return;
	}

}
