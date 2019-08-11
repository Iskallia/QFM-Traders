package kaptainwutax.traders;

import kaptainwutax.traders.event.EventMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Traders.MOD_ID, name = Traders.MOD_NAME, version = Traders.MOD_VERSION)
public class Traders {

	@Instance
	private static Traders INSTANCE;
	
	public static final String MOD_ID = "qfm_traders";
	public static final String MOD_NAME = "QFM Traders";
	public static final String MOD_VERSION = "1.0";
	
	public static Traders getInstance() {
		return INSTANCE;
	}
	
	@EventHandler
	public void onPreInitialization(FMLPreInitializationEvent event) {
		EventMod.onPreInitialization(event);
	}
	
	@EventHandler
	public void onInitialization(FMLInitializationEvent event) {
		EventMod.onInitialization(event);
	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Traders.MOD_ID, name);
	}
	
}
