package kaptainwutax.traders.util;

import com.google.gson.annotations.Expose;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class Product {
	
	protected Item itemCache;
	protected NBTTagCompound nbtCache;
	
	@Expose protected String name;
	@Expose protected int metadata;
	@Expose protected String nbt;
	@Expose protected int amount;
	
	public Product(Item item, int metadata, int amount, NBTTagCompound nbt) {
		this.itemCache = item;
		if(this.itemCache != null)this.name = item.getRegistryName().toString();
		this.metadata = metadata;
		this.nbtCache = nbt;
		if(this.nbtCache != null)this.nbt = this.nbtCache.toString();
		this.amount = amount;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMetadata() {
		return this.metadata;
	}
	
	public NBTTagCompound getNBT() {
		if(this.nbt == null)return null;
        try {if(this.nbtCache == null)this.nbtCache = JsonToNBT.getTagFromJson(this.nbt);} 
        catch(NBTException e) {this.nbtCache = null; System.out.println("Unknown NBT for item " + this.name + ".");}
		return this.nbtCache;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public Item getItem() {
		if(this.itemCache != null)return this.itemCache;		
		this.itemCache = Item.getByNameOrId(this.name);
		if(this.itemCache == null)System.out.println("Unknown item " + this.name + ".");
		return this.itemCache;
	}
	
	public ItemStack toStack() {
		ItemStack stack = new ItemStack(this.getItem(), this.getAmount(), this.getMetadata());
		stack.setTagCompound(this.getNBT());
		return stack;
	}	
	
	public boolean isValid() {
		if(this.getAmount() <= 0)return false;
		if(this.getMetadata() < 0)return false;
		if(this.getItem() == null)return false;
		if(this.getItem() == Items.AIR)return false;
		if(this.getAmount() > this.getItem().getItemStackLimit())return false;
		if(this.nbt != null && this.getNBT() == null)return false;
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		else if(obj == this)return true;
		else if(this.getClass() != obj.getClass())return false;
		
		Product product = (Product)obj;
		
		return product.getItem() == this.getItem() && 
				product.getMetadata() == this.getMetadata() && 
				this.getNBT().equals(product.getNBT());
	}	
	
}