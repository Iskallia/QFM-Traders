package kaptainwutax.traders.init;

import kaptainwutax.traders.Traders;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class InitSoundEvent {

	public static SoundEvent VILLAGER_VANISH;
	
    public static void registerSoundEvents() {
    	VILLAGER_VANISH = registerSoundEvent("entity.trader.villager_vanish");
    }
	
    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation location = Traders.getResource(name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
    
}
