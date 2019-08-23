package kaptainwutax.traders.event;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.command.CommandResetTime;
import kaptainwutax.traders.handler.HandlerGui;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitEntity;
import kaptainwutax.traders.init.InitPacket;
import kaptainwutax.traders.init.InitSpawner;
import kaptainwutax.traders.init.InitTrade;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class EventMod {

	public static void onConstruction(FMLConstructionEvent event) {	
	}
	
	public static void onPreInitialization(FMLPreInitializationEvent event) {
		InitEntity.registerEntities();
		
		if(event.getSide() == Side.CLIENT) {
			InitEntity.registerEntityRenderers();
		}			
	}

	public static void onInitialization(FMLInitializationEvent event) {
		InitPacket.registerPackets();
		NetworkRegistry.INSTANCE.registerGuiHandler(Traders.getInstance(), new HandlerGui());
	}
	
	public static void onPostInitialization(FMLPostInitializationEvent event) {
		InitConfig.registerConfigs();	
		InitTrade.registryTrades();
		InitSpawner.registerSpawners();
	}

	public static void onServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandResetTime());	
	}

}

