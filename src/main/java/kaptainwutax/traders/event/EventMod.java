package kaptainwutax.traders.event;

import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.entity.render.RenderTrader;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitEntity;
import kaptainwutax.traders.init.InitTrades;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventMod {

	public static void onPreInitialization(FMLPreInitializationEvent event) {
		InitEntity.registerEntities();
		
		if(event.getSide() == Side.CLIENT) {
			InitEntity.registerEntityRenderer(EntityTrader.class, RenderTrader.getRenderFactory());
		}
	}

	public static void onInitialization(FMLInitializationEvent event) {
			InitConfig.registerConfigs();	
	}

}
