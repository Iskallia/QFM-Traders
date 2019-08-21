package kaptainwutax.traders.event.world;

import kaptainwutax.traders.Traders;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Traders.MOD_ID)
public class EventWorldUnload {
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Unload event) {
		
	}
	
}
