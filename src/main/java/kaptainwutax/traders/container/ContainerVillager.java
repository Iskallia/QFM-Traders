package kaptainwutax.traders.container;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;

public class ContainerVillager extends ContainerMerchant {

    private IMerchant merchant;
    private World world;
	private EntityPlayer player;

    public ContainerVillager(InventoryPlayer playerInventory, IMerchant merchant, World worldIn) {
    	super(playerInventory, merchant, worldIn);
        this.merchant = merchant;
        this.world = worldIn;   
        this.player = playerInventory.player;
        
        InventoryMerchant inv = this.getMerchantInventory();
                
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();      
        
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

    @Override
	public void setCurrentRecipeIndex(int currentRecipeIndex)
    {
    	int index = currentRecipeIndex & ((1 << 31) - 1);    
    	super.setCurrentRecipeIndex(index);   	

        MerchantRecipe recipe = this.merchant.getRecipes(null).get(index);  

        this.cramStack(0, recipe.getItemToBuy().copy(), currentRecipeIndex >>> 31 == 1);
        if(recipe.hasSecondItemToBuy())this.cramStack(1, recipe.getSecondItemToBuy().copy(), currentRecipeIndex >>> 31 == 1);
    }
    
    private void cramStack(int slotId, ItemStack buy, boolean shift) {
        InventoryPlayer playerInv = this.player.inventory;
        
    	//Remove stack.
        ItemStack stack = this.inventorySlots.get(slotId).getStack();
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
        
        this.inventorySlots.get(slotId).putStack(buy);
    }
    
    public class SlotVillagerResult extends Slot
    {
        /** Merchant's inventory. */
        private final InventoryMerchant merchantInventory;
        /** The Player whos trying to buy/sell stuff. */
        private final EntityPlayer player;
        private int removeCount;
        /** "Instance" of the Merchant. */
        private final IMerchant merchant;

        public SlotVillagerResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition)
        {
            super(merchantInventory, slotIndex, xPosition, yPosition);
            this.player = player;
            this.merchant = merchant;
            this.merchantInventory = merchantInventory;
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         */
        @Override
		public boolean isItemValid(ItemStack stack)
        {
            return false;
        }

        /**
         * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
         * stack.
         */
        @Override
		public ItemStack decrStackSize(int amount)
        {
            if (this.getHasStack())
            {
                this.removeCount += Math.min(amount, this.getStack().getCount());
            }

            return super.decrStackSize(amount);
        }

        /**
         * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
         * internal count then calls onCrafting(item).
         */
        @Override
		protected void onCrafting(ItemStack stack, int amount)
        {
            this.removeCount += amount;
            this.onCrafting(stack);
        }

        /**
         * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
         */
        @Override
		protected void onCrafting(ItemStack stack)
        {
            stack.onCrafting(this.player.world, this.player, this.removeCount);
            this.removeCount = 0;
        }

        @Override
		public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
        {
        	System.out.println("Taking.");
            this.onCrafting(stack);
            MerchantRecipe merchantrecipe = this.merchantInventory.getCurrentRecipe();

            if (merchantrecipe != null)
            {
            	System.out.println("Valid recipe.");
                ItemStack itemstack = this.merchantInventory.getStackInSlot(0);
                ItemStack itemstack1 = this.merchantInventory.getStackInSlot(1);

                if (this.doTrade(merchantrecipe, itemstack, itemstack1) || this.doTrade(merchantrecipe, itemstack1, itemstack))
                {
                	System.out.println("Did trade.");
                    this.merchant.useRecipe(merchantrecipe);
                    thePlayer.addStat(StatList.TRADED_WITH_VILLAGER);
                    this.merchantInventory.setInventorySlotContents(0, itemstack);
                    this.merchantInventory.setInventorySlotContents(1, itemstack1);
                }
            }

            return stack;
        }

        private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem)
        {
            ItemStack itemstack = trade.getItemToBuy();
            ItemStack itemstack1 = trade.getSecondItemToBuy();

            if (firstItem.getItem() == itemstack.getItem() && firstItem.getCount() >= itemstack.getCount())
            {
                if (!itemstack1.isEmpty() && !secondItem.isEmpty() && itemstack1.getItem() == secondItem.getItem() && secondItem.getCount() >= itemstack1.getCount())
                {
                    firstItem.shrink(itemstack.getCount());
                    secondItem.shrink(itemstack1.getCount());
                    return true;
                }

                if (itemstack1.isEmpty() && secondItem.isEmpty())
                {
                    firstItem.shrink(itemstack.getCount());
                    return true;
                }
            }

            return false;
        }
    }
  
}
