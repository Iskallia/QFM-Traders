package kaptainwutax.traders.init;

import kaptainwutax.traders.Traders;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class InitSoundEvent {

	public static SoundEvent ISKALL_LAUGH;
	
    public static void registerSoundEvents() {
    	ISKALL_LAUGH = registerSoundEvent("entity.trader.iskall_laugh");
    }
	
    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation location = Traders.getResource(name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
    
}
