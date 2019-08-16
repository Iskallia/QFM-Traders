package kaptainwutax.traders.event;

import kaptainwutax.traders.init.InitSoundEvent;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventRegistry {
	
	@SubscribeEvent	   
	public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
		InitSoundEvent.registerSoundEvents();
	}
	   
}
