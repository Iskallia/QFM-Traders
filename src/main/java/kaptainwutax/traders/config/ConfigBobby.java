package kaptainwutax.traders.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.util.Product;
import kaptainwutax.traders.util.Trade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ConfigBobby extends ConfigTrades {

	@Expose public List<String> NAMES = new ArrayList<String>();
	
	@Expose public List<String> GREETING_PHRASES = new ArrayList<String>();
	//@Expose public List<String> LEAVING_PHRASES = new ArrayList<String>();
	
	@Expose public int DESPAWN_DELAY;
	
	@Expose public int TRADES_COUNT;
	
	public ConfigBobby() {

	}

	@Override
	public String getLocation() {
		return "bobby.json";
	}

	@Override
	protected void resetConfig() {
		this.NAMES.add("Bobby");
		
		this.GREETING_PHRASES.add("Psst, <username>, I have arrived to you! Wanna trade?");
		//this.LEAVING_PHRASES.add("Alright, <username>, I have to go now.");
		
		this.DESPAWN_DELAY = 24000 * 5;
		
		this.TRADES_COUNT = 5;
		
		this.DEFAULT_TRADE = new Trade(null, null, null, 0);		
		this.CUSTOM_TRADES.add(new Trade(new Product(Items.APPLE, 0, 8, null), null, new Product(Items.GOLDEN_APPLE, 0, 1, null), 5));
		this.CUSTOM_TRADES.add(new Trade(new Product(Items.GOLDEN_APPLE, 0, 8, null), null, new Product(Items.GOLDEN_APPLE, 1, 1, null), 2));
		
        NBTTagCompound nbt = new NBTTagCompound();
        
        NBTTagList enchantments = new NBTTagList();
        
        NBTTagCompound knockback = new NBTTagCompound();
        knockback.setShort("id", (short)Enchantment.getEnchantmentID(Enchantments.KNOCKBACK));
        knockback.setShort("lvl", (short)10);
        enchantments.appendTag(knockback);      

        nbt.setTag("ench", enchantments);
		
		this.CUSTOM_TRADES.add(new Trade(new Product(Items.GOLDEN_APPLE, 1, 8, null), null, new Product(Items.STICK, 0, 1, nbt), 10));
	}

}