package kaptainwutax.traders.util;

import com.google.gson.annotations.Expose;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Product {
	
	protected Item item;
	@Expose protected String name;
	@Expose protected int metadata;
	@Expose protected int amount;
	
	public Product(Item item, int metadata, int amount) {
		this.item = item;
		if(this.item != null)this.name = item.getRegistryName().toString();
		this.metadata = metadata;
		this.amount = amount;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMetadata() {
		return this.metadata;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public Item getItem() {
		if(this.item != null)return this.item;		
		this.item = Item.getByNameOrId(this.name);
		if(this.item == null)System.out.println("Unknown item " + this.name + ".");
		return this.item;
	}
	
	public ItemStack toStack() {
		return new ItemStack(this.getItem(), this.getAmount(), this.getMetadata());
	}	
	
	public boolean isValid() {
		if(this.getItem() == null)return false;
		if(this.getItem() == Items.AIR)return false;
		if(this.getAmount() <= 0)return false;
		if(this.getAmount() > this.getItem().getItemStackLimit())return false;
		if(this.getMetadata() < 0)return false;
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		else if(obj == this)return true;
		else if(this.getClass() != obj.getClass())return false;
		
		Product product = (Product)obj;
		
		return product.getItem() == this.getItem() && product.getMetadata() == this.getMetadata();
	}	
	
}
