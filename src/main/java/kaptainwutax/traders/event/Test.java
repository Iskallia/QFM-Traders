package kaptainwutax.traders.event;

import kaptainwutax.traders.util.Time;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@EventBusSubscriber
public class Test {

	@SubscribeEvent
	public static void tick(PlayerTickEvent event) {
		Time.updateTime(event.player.world.getTotalWorldTime());
	}
	
}
