package kaptainwutax.traders;

import com.google.gson.annotations.Expose;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Trade {
	
	private Item productCache;
	
	@Expose protected String product;
	@Expose protected int value;
	@Expose protected int maxUses;
	private int hashCode;
	private boolean isDefault = false;
	
	public Trade() {
		//Serialization.
	}
	
	public Trade(Item product) {
		this(product, 1, 1);
		this.isDefault = true;
	}

	public Trade(Item product, int value, int maxUses) {
		this.product = product.getRegistryName().toString();
		this.value = value;
		this.maxUses = maxUses;
	}

	public Item getProduct() {
		if(this.productCache != null)return this.productCache;		
		this.productCache = Item.getByNameOrId(this.product);
		if(this.productCache == null)this.productCache = Item.getItemFromBlock(Block.getBlockFromName(this.product));
		if(this.productCache == null)System.out.println("Unknown item " + product);
		return this.productCache;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getMaxUses() {
		return this.maxUses;
	}
	
	public boolean isDefault() {
		return this.isDefault;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		else if(obj == this)return true;
		else if(this.getClass() != obj.getClass())return false;
		
		Trade trade = (Trade)obj;
		return trade.getProduct() == this.getProduct();
	}	
	
	@Override
	public int hashCode() {
		if(this.hashCode == 0) {
			this.hashCode = this.product.hashCode();
		}
		
		return this.hashCode;
	}
	
	private class Product {
		
		
		
		public Product(Item item, int metadata, int amount) {
		}
		
	}

}
