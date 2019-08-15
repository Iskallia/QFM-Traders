package kaptainwutax.traders.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.netty.buffer.Unpooled;
import kaptainwutax.traders.Product;
import kaptainwutax.traders.Trade;
import kaptainwutax.traders.Traders;
import kaptainwutax.traders.container.ContainerVillager;
import kaptainwutax.traders.gui.GuiContainerVillager;
import kaptainwutax.traders.handler.HandlerGui;
import kaptainwutax.traders.util.Pair;
import kaptainwutax.traders.util.Time;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityTrader extends EntityVillager {

	private String name = "Trader";
	private List<Trade> possibleTrades;
	
	public MerchantRecipeList tradesCache = null;
	public MerchantRecipeList trades = null;
	private boolean tradesDirty;
	
	public long lastRestockWeek = -1;
	
	public ItemStackHandler inventory = new ItemStackHandler(54);

	public EntityTrader(World world) {
		super(world);
	}
	
	public EntityTrader(World world, String name, List<Trade> possibleTrades) {
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
		if(this.world.isRemote)return;
		
		this.lastRestockWeek = currentWeek;
		
   	 	this.trades = new MerchantRecipeList();
 	
   	 	ArrayList<Trade> randomTrades = new ArrayList<Trade>(this.possibleTrades);
   	 	Collections.shuffle(randomTrades);
   	
		for(int i = 0; i < Math.min(randomTrades.size(), 10); i++) {
			Trade trade = randomTrades.get(i);
				
			ItemStack buy = trade.getBuy().toStack();
			ItemStack extra = trade.getExtra() == null ? null : trade.getExtra().toStack();
			ItemStack sell = trade.getSell().toStack();
			
			MerchantRecipe recipe = new CustomMerchantRecipe(buy, extra, sell, trade.getMaxUses());
			
			this.trades.add(recipe);						
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

	public void setTrades(MerchantRecipeList trades) {
		this.tradesCache = trades;
		this.tradesDirty = true;
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
		compound.setLong("lastRestockWeek", this.lastRestockWeek);		
		compound.setTag("Inventory", this.inventory.serializeNBT());	
		if(compound.hasKey("Offers"))compound.removeTag("Offers");		
		if(trades != null)compound.setTag("Offers", this.trades.getRecipiesAsTags());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		      
        this.lastRestockWeek = compound.getLong("lastRestockWeek");
		this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
		
		 if(compound.hasKey("Offers", 10)) {
			 NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
	         this.trades = new MerchantRecipeList(nbttagcompound);           
	     } else {
	    	 this.restock(-1);
	    }
	}
	
	@Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {	
		if(this.tradesDirty) {
			this.trades = new MerchantRecipeList();
			
			for(MerchantRecipe recipe: this.tradesCache) {
				this.trades.add(new CustomMerchantRecipe(recipe.getItemToBuy(), recipe.getSecondItemToBuy(), recipe.getItemToSell(), recipe.getMaxTradeUses(), recipe.getToolUses()));
			}
			
			this.tradesDirty = false;
		}
		
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
            		this.displayVillagerTradeGui((EntityPlayerMP)player);
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
	
    public void displayVillagerTradeGui(EntityPlayerMP player) {
    	player.getNextWindowId();
    	player.openContainer = new ContainerVillager(player.inventory, this, player.world);
    	player.openContainer.windowId = player.currentWindowId;
    	player.openContainer.addListener(player);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(player, player.openContainer));
        IInventory iinventory = ((ContainerVillager)player.openContainer).getMerchantInventory();
        ITextComponent itextcomponent = this.getDisplayName();
        player.connection.sendPacket(new SPacketOpenWindow(player.currentWindowId, "minecraft:villager", itextcomponent, iinventory.getSizeInventory()));
        MerchantRecipeList merchantrecipelist = this.getRecipes(player);

        if(merchantrecipelist != null) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(player.currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            player.connection.sendPacket(new SPacketCustomPayload("MC|TrList", packetbuffer));
        }
    }
		
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
	
}
