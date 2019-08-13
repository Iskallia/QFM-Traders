package kaptainwutax.traders;

import com.google.gson.annotations.Expose;

import net.minecraft.item.Item;

public class Product {
	
	protected Item item;
	@Expose protected String name;
	@Expose protected int metadata;
	@Expose protected int amount;
	
	public Product(Item item, int metadata) {
		this(item, metadata, 0);
	}
	
	public Product(Item item, int metadata, int amount) {
		this.item = item;
		this.name = item.getRegistryName().toString();
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		else if(obj == this)return true;
		else if(this.getClass() != obj.getClass())return false;
		
		Product product = (Product)obj;
		
		if(product.getMetadata() == -1 || this.getMetadata() == -1) {
			return product.getItem() == this.getItem();
		}
		
		return product.getItem() == this.getItem() && product.getMetadata() == this.getMetadata();
	}	
	
}
