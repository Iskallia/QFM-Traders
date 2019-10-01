package kaptainwutax.traders.event;

import kaptainwutax.traders.init.InitBlock;
import kaptainwutax.traders.init.InitItem;
import kaptainwutax.traders.init.InitSoundEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventRegistry {
	
	@SubscribeEvent	   
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		InitBlock.registerBlocks(event.getRegistry());
	}
	
	@SubscribeEvent	   
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		InitItem.registerItems(event.getRegistry());
	}
	
	@SubscribeEvent	   
	public static void onModelRegister(ModelRegistryEvent event) {
		Item item = Item.getItemFromBlock(InitBlock.TRADING_TABLE);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@SubscribeEvent	   
	public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
		InitSoundEvent.registerSoundEvents();
	}
	   
}
