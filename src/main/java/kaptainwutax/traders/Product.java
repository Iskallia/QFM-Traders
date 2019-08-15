package kaptainwutax.traders;

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
		return new ItemStack(this.item, this.amount, this.metadata);
	}	
	
	public boolean isValid() {
		if(item == null)return false;
		if(item == Items.AIR)return false;
		if(this.amount <= 0)return false;
		if(this.amount > item.getItemStackLimit())return false;
		if(this.metadata < 0)return false;
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
