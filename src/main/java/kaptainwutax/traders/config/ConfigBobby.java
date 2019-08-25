package kaptainwutax.traders.config;

import kaptainwutax.traders.util.Product;
import kaptainwutax.traders.util.Trade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ConfigBobby extends ConfigTrades {

	public ConfigBobby() {

	}

	@Override
	public String getLocation() {
		return "bobby.json";
	}

	@Override
	protected void resetConfig() {
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