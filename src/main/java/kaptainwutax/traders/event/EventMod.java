package kaptainwutax.traders.event;

import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.entity.render.RenderTrader;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitEntity;
import kaptainwutax.traders.init.InitTrades;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventMod {

	public static void onConstruction(FMLConstructionEvent event) {	
	}
	
	public static void onPreInitialization(FMLPreInitializationEvent event) {
		InitEntity.registerEntities();
		
		if(event.getSide() == Side.CLIENT) {
			InitEntity.registerEntityRenderer(EntityTrader.class, RenderTrader.getRenderFactory());
		}			
	}

	public static void onInitialization(FMLInitializationEvent event) {
	}
	
	public static void onPostInitialization(FMLPostInitializationEvent event) {
		InitConfig.registerConfigs();	
		InitTrades.registryTrades(InitConfig.CONFIG_TRADES);
	}

}
