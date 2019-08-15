package kaptainwutax.traders.container;

import io.netty.buffer.Unpooled;
import kaptainwutax.traders.init.InitPacket;
import kaptainwutax.traders.net.packet.PacketS2CSyncTrades;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class ContainerVillager extends ContainerMerchant {

    private final IMerchant merchant;
    private final World world;
    private boolean needsUpdate = true;
	private EntityPlayer player;

    public ContainerVillager(InventoryPlayer playerInventory, IMerchant merchant, World worldIn) {
    	super(playerInventory, merchant, worldIn);
        this.merchant = merchant;
        this.world = worldIn;   
        this.player = playerInventory.player;
        
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();
        
        InventoryMerchant inv = this.getMerchantInventory();
        
        this.addSlotToContainer(new Slot(inv, 0, 136, 37));
        this.addSlotToContainer(new Slot(inv, 1, 162, 37));
        this.addSlotToContainer(new SlotMerchantResult(player, merchant, inv, 2, 220, 37));

        int int_4;
        for(int_4 = 0; int_4 < 3; ++int_4) {
           for(int int_3 = 0; int_3 < 9; ++int_3) {
              this.addSlotToContainer(new Slot(playerInventory, int_3 + int_4 * 9 + 9, 108 + int_3 * 18, 84 + int_4 * 18));
           }
        }

        for(int_4 = 0; int_4 < 9; ++int_4) {
           this.addSlotToContainer(new Slot(playerInventory, int_4, 108 + int_4 * 18, 142));
        }
    }
    
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);      
    }

    public void setCurrentRecipeIndex(int currentRecipeIndex)
    {
    	int index = currentRecipeIndex & ((1 << 31) - 1);
        this.getMerchantInventory().setCurrentRecipeIndex(index);       

        MerchantRecipe recipe = this.merchant.getRecipes(null).get(index);  

        this.cramStack(0, recipe.getItemToBuy(), currentRecipeIndex >>> 31 == 1);
        if(recipe.hasSecondItemToBuy())this.cramStack(1, recipe.getSecondItemToBuy(), currentRecipeIndex >>> 31 == 1);
    }
    
    private void cramStack(int slotId, ItemStack buy, boolean shift) {
        InventoryPlayer playerInv = this.player.inventory;
        
    	//Remove stack.
        ItemStack stack = this.inventorySlots.get(0).getStack();
        boolean same = true;
        
        if(stack.getItem() != buy.getItem() || stack.getMetadata() != buy.getMetadata()) {
        	same = false;
        	if(!stack.isEmpty() && !this.player.inventory.addItemStackToInventory(stack))return;
        }
        
        //Add stack.
        if(shift)buy.setCount(buy.getMaxStackSize());                     
        if(same)buy.setCount(buy.getCount() + stack.getCount());
        
        buy.setCount(MathHelper.clamp(buy.getCount(), 0, buy.getMaxStackSize()));
        
        int count = 0;
        if(same)count += stack.getCount();
        
        for(int i = 0; i < playerInv.getSizeInventory() && count < buy.getCount(); i++) {
        	ItemStack slotStack = playerInv.getStackInSlot(i);        	
        	if(slotStack.getItem() != buy.getItem() || slotStack.getMetadata() != buy.getMetadata())continue;
        	count += playerInv.decrStackSize(i, buy.getCount() - count).getCount();
        }
        
        buy.setCount(count);
        
        this.inventorySlots.get(0).putStack(buy);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.merchant.getCustomer() == playerIn;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {   
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 0 && index != 1)
            {
                if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
    }
  
}
