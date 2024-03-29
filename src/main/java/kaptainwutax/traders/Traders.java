package kaptainwutax.traders;

import kaptainwutax.traders.event.EventMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Traders.MOD_ID, name = Traders.MOD_NAME, version = Traders.MOD_VERSION)
public class Traders {

	@Mod.Instance
	private static Traders INSTANCE;
	
	public static final String MOD_ID = "qfm_traders";
	public static final String MOD_NAME = "QFM Traders";
	public static final String MOD_VERSION = "0.3.3";
	
	public static Traders getInstance() {
		return INSTANCE;
	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Traders.MOD_ID, name);
	}
	
	@Mod.EventHandler
	public void onConstruction(FMLConstructionEvent event) {
		EventMod.onConstruction(event);
	}
	
	@Mod.EventHandler
	public void onInitialization(FMLInitializationEvent event) {
		EventMod.onInitialization(event);
	}
	
	@Mod.EventHandler
	public void onPostInitialization(FMLPostInitializationEvent event) {
		EventMod.onPostInitialization(event);
	}
	
	@Mod.EventHandler
	public void onPreInitialization(FMLPreInitializationEvent event) {
		EventMod.onPreInitialization(event);
	}
	
	@Mod.EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		EventMod.onServerStart(event);
	}
	
}
