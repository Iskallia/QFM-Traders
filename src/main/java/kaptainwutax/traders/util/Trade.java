package kaptainwutax.traders.util;

import com.google.gson.annotations.Expose;

public class Trade {
	
	@Expose protected Product buy;
	@Expose protected Product extra;
	@Expose protected Product sell;
	@Expose protected int maxUses;
	private int hashCode;

	private Trade() {
		//Serialization.
	}
	
	public Trade(Product buy, Product extra, Product sell, int maxUses) {
		this.buy = buy;
		this.extra = extra;
		this.sell = sell;
		this.maxUses = maxUses;
	}
	
	public Product getBuy() {
		return this.buy;
	}
	
	public Product getExtra() {
		return this.extra;
	}		

	public Product getSell() {
		return this.sell;
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

	public boolean isValid() {
		if(this.maxUses <= 0)return false;
		if(this.buy == null || !this.buy.isValid())return false;
		if(this.sell == null || !this.sell.isValid())return false;
		if(this.extra != null && !this.extra.isValid())return false;
		return true;
	}

	public boolean hasProduct(Product disallowed) {
		if(this.buy.equals(disallowed))return true;
		if(this.extra != null && this.extra.equals(disallowed))return true;
		if(this.sell.equals(disallowed))return true;
		return false;
	}
	
}
