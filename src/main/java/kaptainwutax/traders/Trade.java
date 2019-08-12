package kaptainwutax.traders;

import com.google.gson.annotations.Expose;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class Trade {
	
	private Item productCache;
	
	@Expose protected String product;
	@Expose protected int value;
	@Expose protected int maxUses;
	private int hashCode;

	public Trade() {
		//Serialization.
	}
	
	public Trade(Item product) {
		this(product, 1, 1);
	}

	public Trade(Item product, int value, int maxUses) {
		this.product = product.getRegistryName().toString();
		this.value = value;
		this.maxUses = maxUses;
	}

	public Item getProduct() {
		if(this.productCache != null)return this.productCache;
		
		for(Item item: Item.REGISTRY) {
			if(item.getRegistryName().toString().equals(this.product)) {
				this.productCache = item;
				return this.productCache;
			}
		}
		
		throw new IllegalArgumentException("Unknown Item [" + this.product + "]!");
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getMaxUses() {
		return this.maxUses;
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
	
}
