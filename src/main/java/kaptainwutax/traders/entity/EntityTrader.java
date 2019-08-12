package kaptainwutax.traders.entity;

import kaptainwutax.traders.Trade;
import kaptainwutax.traders.init.InitTrades;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityTrader extends EntityVillager {

	public MerchantRecipeList trades = null;
	public boolean firstSave = true;
	
	public EntityTrader(World world) {
		super(world);	
		this.setProfession(5);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);		
		
		if(compound.hasKey("Offers"))compound.removeTag("Offers");
		
		if(trades != null) {
			compound.setTag("Offers", this.trades.getRecipiesAsTags());
		} 		
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		
        if (compound.hasKey("Offers", 10)) {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
            this.trades = new MerchantRecipeList(nbttagcompound);           
        } else {
        	this.trades = new MerchantRecipeList();
        	
    		for(Trade trade: InitTrades.TRADES.values()) {
    			if(trade.isDefault())continue;
    			ItemStack buy = trade.getBuyStack();
    			ItemStack sell = trade.getSellStack();
    			
    			MerchantRecipe trade1 = new CustomMerchantRecipe(buy, sell, trade.getMaxUses());
    			MerchantRecipe trade2 = new CustomMerchantRecipe(sell, buy, trade.getMaxUses());
    			
    			this.trades.add(trade1);						
    			if(trade.useFlippedTrade())this.trades.add(trade2);
    		}
       }
	}
	
	@Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {	
		return this.trades;
    }
	
	public class CustomMerchantRecipe extends MerchantRecipe {
		
		public CustomMerchantRecipe(ItemStack buy, ItemStack sell, int maxUses) {
			super(buy, ItemStack.EMPTY, sell, 0, maxUses);		
		}
		
		public CustomMerchantRecipe(NBTTagCompound nbttagcompound) {
			super(nbttagcompound);
		}
		
		@Override
	    public void increaseMaxTradeUses(int increment) {
	        return;
	    }
		
	}
}
