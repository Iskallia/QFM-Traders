package kaptainwutax.traders.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.netty.buffer.Unpooled;
import kaptainwutax.traders.Traders;
import kaptainwutax.traders.container.ContainerVillager;
import kaptainwutax.traders.entity.ai.EntityTraderAILeave;
import kaptainwutax.traders.entity.render.ILayeredTextures;
import kaptainwutax.traders.handler.HandlerGui;
import kaptainwutax.traders.util.CustomMerchantRecipe;
import kaptainwutax.traders.util.Time;
import kaptainwutax.traders.util.Trade;
import kaptainwutax.traders.world.data.WorldDataTime;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
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

public abstract class EntityTrader extends EntityVillager implements ILayeredTextures {

	public static Set<String> UUIDS = new HashSet<String>();
	
	public MerchantRecipeList tradesCache = null;
	public MerchantRecipeList trades = null;
	private boolean tradesDirty;
	
	public long lastRestockWeek = -1;
	
	public ItemStackHandler inventory = new ItemStackHandler(54);
	
	public EntityTrader(World world, String name) {
		super(world);	
		if(name != null)this.setCustomNameTag(name);		
		this.setAlwaysRenderNameTag(true);
	}
	
	@Override	
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityTraderAILeave(this));
		super.initEntityAI();
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory : super.getCapability(capability, facing);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();

		UUIDS.add(this.getUniqueID().toString());
		
		if(world.isRemote)return;
		
		this.doInventoryTrades();
		
		WorldDataTime data = WorldDataTime.get(world);
		Time time = data.getTime();
		int currentWeek = time.WEEK;	
		
		if(lastRestockWeek == -1 || lastRestockWeek != currentWeek) {
			this.restock(currentWeek);
		}
	}
	
	@Override
	public void useRecipe(MerchantRecipe recipe) {
		super.useRecipe(recipe);
	}
	
	private void restock(int currentWeek) {
		if(this.world.isRemote)return;
		
		this.lastRestockWeek = currentWeek;
		
   	 	this.trades = new MerchantRecipeList();
 	
   	 	List<Trade> randomTrades = this.getNewTrades();
   	
		for(Trade trade: randomTrades) {				
			ItemStack buy = trade.getBuy().toStack();
			ItemStack extra = trade.getExtra() == null ? null : trade.getExtra().toStack();
			ItemStack sell = trade.getSell().toStack();
			
			MerchantRecipe recipe = new CustomMerchantRecipe(buy, extra, sell, trade.getMaxUses());
			
			this.trades.add(recipe);						
		}
	}
	
	public abstract List<Trade> getNewTrades();

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
		
		UUIDS.remove(this.getUniqueID().toString());
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
	
}
