package kaptainwutax.traders;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.util.Pair;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Trade {
	
	@Expose protected Product sell;
	@Expose protected Product buy;
	@Expose protected int maxUses;
	@Expose protected boolean useFlippedTrade;
	private boolean isDefault = false;
	private int hashCode;

	private Trade() {
		//Serialization.
	}
	
	public Trade(Product sell, Product buy, int maxUses, boolean useFlippedTrade) {
		this.sell = sell;
		this.buy = buy;
		this.maxUses = maxUses;
		this.useFlippedTrade = useFlippedTrade;
	}

	public Trade(Item sellingItem, int metadata, Trade defaultTrade) {
		this(new Product(sellingItem, metadata, defaultTrade.getSell().getAmount()), 
				new Product(Items.DIAMOND, 0, defaultTrade.getBuy().getAmount()), 
				defaultTrade.getMaxUses(), defaultTrade.useFlippedTrade());
		this.isDefault = true;
	}

	public Product getSell() {
		return this.sell;
	}
	
	public Product getBuy() {
		return this.buy;
	}
	
	public ItemStack getSellStack() {
		return new ItemStack(this.sell.getItem(), this.sell.getAmount(), this.sell.getMetadata());
	}
	
	public ItemStack getBuyStack() {
		return new ItemStack(this.buy.getItem(), this.buy.getAmount(), this.buy.getMetadata());
	}
	
	public int getMaxUses() {
		return this.maxUses;
	}
	
	public boolean useFlippedTrade() {
		return this.useFlippedTrade;
	}
	
	public boolean isDefault() {
		return this.isDefault;
	}
	
	public Pair<Product, Product> getKey() {
		return new Pair<Product, Product>(this.sell, this.buy);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		else if(obj == this)return true;
		else if(this.getClass() != obj.getClass())return false;
		
		Trade trade = (Trade)obj;
		return trade.sell.equals(this.sell) && trade.buy.equals(this.buy);
	}	
	
	@Override
	public int hashCode() {
		if(this.hashCode == 0) {
			this.hashCode = this.sell.getItem().getRegistryName().getResourcePath().hashCode();
			this.hashCode += this.buy.getItem().getRegistryName().getResourcePath().hashCode() * 97;
		}
		
		return this.hashCode;
	}
	
}
