package kaptainwutax.traders.init;

import kaptainwutax.traders.block.BlockTradingTable;
import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlock {

	public static BlockTradingTable TRADING_TABLE = new BlockTradingTable("trading_table");

	private static void registerBlock(Block block, IForgeRegistry<Block> registry) {
		registry.register(block);
	}
	
	public static void registerBlocks(IForgeRegistry<Block> registry) {
		registerBlock(TRADING_TABLE, registry);
	}

}
