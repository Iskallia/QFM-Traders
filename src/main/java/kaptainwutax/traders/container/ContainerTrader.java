package kaptainwutax.traders.container;

import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTrader extends Container {

	private EntityTrader trader;

	public ContainerTrader(InventoryPlayer playerInventory, EntityTrader trader) {
		if(trader == null)return;
		this.trader = trader;
		if(!trader.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))return;
		
		IItemHandler inventory = trader.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		for(int row = 0; row < 6; row++) {
			for(int column = 0; column < 9; column++) {
				int slotId = row * 9 + column;				
				this.addSlotToContainer(new SlotItemHandler(trader.inventory, slotId, 8 + column * 18, 18 + row * 18));
			}
		}
		
		for(int row = 0; row < 3; row++) {
			for(int column = 0; column < 9; column++) {
				int slotId = row * 9 + column + 9;				
				this.addSlotToContainer(new Slot(playerInventory, slotId, 8 + column * 18, 140 + row * 18));
			}
		}
		
		for(int hotbar = 0; hotbar < 9; hotbar++) {
			this.addSlotToContainer(new Slot(playerInventory, hotbar, 8 + hotbar * 18, 198));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();

            if(index < containerSlots) {
                if(!this.mergeItemStack(stackInSlot, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!this.mergeItemStack(stackInSlot, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if(stackInSlot.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            slot.onTake(player, stackInSlot);

        }
        
		return stack;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		this.trader.setCustomer(null);
	}

}
