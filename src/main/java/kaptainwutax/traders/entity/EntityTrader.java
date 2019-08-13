package kaptainwutax.traders.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import kaptainwutax.traders.Traders;
import kaptainwutax.traders.handler.HandlerGui;
import kaptainwutax.traders.util.Pair;
import kaptainwutax.traders.util.Time;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityTrader extends EntityVillager {

	private String name = "Trader";
	private Map<Pair<Product, Product>, Trade> possibleTrades;
	
	public MerchantRecipeList trades = null;
	public long lastRestockWeek = -1;
	
	public ItemStackHandler inventory = new ItemStackHandler(54);

	public EntityTrader(World world, String name, Map<Pair<Product, Product>, Trade> possibleTrades) {
		super(world);	
		if(name != null)this.name = name;
		this.possibleTrades = possibleTrades;
		this.setAlwaysRenderNameTag(true);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory : super.getCapability(capability, facing);
	}
	
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(!this.hasCustomName() || !this.getCustomNameTag().equals(this.name)) {
			this.setCustomNameTag(this.name);
		}
		
		this.doInventoryTrades();
		
		Time.updateTime(this.world.getTotalWorldTime());
		int currentWeek = Time.WEEK;	
		
		if(lastRestockWeek == -1 || lastRestockWeek != currentWeek) {
			this.restock(currentWeek);
		}
	}
	
	private void restock(int currentWeek) {
		this.lastRestockWeek = currentWeek;
		
   	 	this.trades = new MerchantRecipeList();
 	
   	 	ArrayList<Trade> randomTrades = new ArrayList<Trade>(this.possibleTrades.values());
   	 	Collections.shuffle(randomTrades);
   	
		for(int i = 0; i < Math.min(randomTrades.size(), 10); i++) {
			Trade trade = randomTrades.get(i);
				
			ItemStack buy = trade.getBuyStack();
			ItemStack sell = trade.getSellStack();
			
			MerchantRecipe trade1 = new CustomMerchantRecipe(sell, buy, trade.getMaxUses());
			MerchantRecipe trade2 = new CustomMerchantRecipe(buy, sell, trade.getMaxUses());
			
			this.trades.add(trade1);						
			if(trade.useFlippedTrade())this.trades.add(trade2);
		}
	}

	private void doInventoryTrades() {
		if(this.trades == null || this.world.isRemote)return;
		
		for(MerchantRecipe recipe: this.trades) {
			if(recipe.isRecipeDisabled())continue;
			
			ItemStack buy = recipe.getItemToSell();
			ItemStack sell = recipe.getItemToBuy();
			
			int sellCount = 0;
			
			for(int i = 0; i < 54; i++) {
				ItemStack stackInSlot = this.inventory.getStackInSlot(i);				
				if(this.inventory.getStackInSlot(i).getItem() != sell.getItem() ||
						this.inventory.getStackInSlot(i).getMetadata() != sell.getMetadata())continue;
				
				sellCount += stackInSlot.getCount();
			}
			
			if(sellCount >= sell.getCount()) {
				sellCount = sell.getCount();

				for(int i = 0; i < 54 && sellCount > 0; i++) {
					ItemStack stackInSlot = this.inventory.getStackInSlot(i);				
					if(this.inventory.getStackInSlot(i).getItem() != sell.getItem() || 
							this.inventory.getStackInSlot(i).getMetadata() != sell.getMetadata())continue;

					ItemStack extracted = this.inventory.extractItem(i, sellCount, false);
					sellCount -= extracted.getCount();
				}
				
				ItemStack buyCopy = buy.copy();
				
				for(int i = 0; i < 54 && !buyCopy.isEmpty(); i++) {							
					buyCopy = this.inventory.insertItem(i, buyCopy, false);
				}
				
				if(!buyCopy.isEmpty()) {
					this.entityDropItem(buyCopy, 0.8f);
				}
				
				recipe.incrementToolUses();
			}		

		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		
		if(this.world.isRemote)return;
		
		for(int i = 0; i < 54; i++) {
			ItemStack stackInSlot = this.inventory.getStackInSlot(i);	
			
			if(!stackInSlot.isEmpty()) {
				this.entityDropItem(stackInSlot, 0.2f);
			}
		}
	}
	
	@Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        Item item = itemstack.getItem();

        for(int i = 0; i < 54 && !itemstack.isEmpty(); i++) {
        	itemstack = this.inventory.insertItem(i, itemstack, false);
        }
        
        if(itemEntity.getItem().getCount() == itemstack.getCount())return;
        
        if (itemstack.isEmpty()) {
            itemEntity.setDead();
        } else {
        	itemEntity.getItem().setCount(itemstack.getCount());
        }
        
        if(world.isRemote)return;
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.4F + 2.0F);
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);		
		
		compound.setTag("Inventory", this.inventory.serializeNBT());
		
		if(compound.hasKey("Offers"))compound.removeTag("Offers");
		
		if(trades != null) {
			compound.setTag("Offers", this.trades.getRecipiesAsTags());
		} 
		
		compound.setLong("lastRestockWeek", this.lastRestockWeek);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		
		this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
		
		 if(compound.hasKey("Offers", 10)) {
			 NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
	         this.trades = new MerchantRecipeList(nbttagcompound);           
	     } else {
	    	 this.restock(-1);
	    }
        
        this.lastRestockWeek = compound.getLong("lastRestockWeek");
	}
	
	@Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {	
		return this.trades;
    }
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean isTag = itemstack.getItem() == Items.NAME_TAG;

        if (isTag) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        } else if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
            if (!this.world.isRemote) {
                this.setCustomer(player);
                
            	if(!player.isSneaking()) {
            		player.displayVillagerTradeGui(this);
            	} else {
            		player.openGui(Traders.getInstance(), HandlerGui.TRADER, world, this.getEntityId(), 0, 0);
            	}
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
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
