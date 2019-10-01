package kaptainwutax.traders.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItem {

	public static ItemBlock ITEM_TRADING_TABLE = new ItemBlock(InitBlock.TRADING_TABLE);
	
	public static void registerItem(Item item, IForgeRegistry<Item> registry) {
		registry.register(item);
	}
	
	public static void registerItemBlock(ItemBlock itemBlock, IForgeRegistry<Item> registry) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		itemBlock.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
		registry.register(itemBlock);
	}
	
	public static void registerItems(IForgeRegistry<Item> registry) {
		registerItemBlock(ITEM_TRADING_TABLE, registry);
	}

}
