package kaptainwutax.traders.init;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.entity.EntityBobby;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.render.RenderTrader;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class InitEntity {

	private static int ID = 0;

	private static int nextId() {
		return InitEntity.ID++;
	}
	
	public static void registerEntities() {
		registerEntityAndEgg("tom", EntityTom.class, 0xFFFFFF, 0x000000);
		registerEntityAndEgg("bobby", EntityBobby.class, 0x000000, 0xFFFFFF);
	}

	private static void registerEntity(String name, Class<? extends Entity> entityClass) {
		EntityRegistry.registerModEntity(Traders.getResource(name), entityClass, 
				name, InitEntity.nextId(), Traders.getInstance(), 64, 1, true
		);
	}
	
	private static void registerEntityAndEgg(String name, Class<? extends Entity> entityClass, int primaryEggColor, int secondaryEggColor) {
		EntityRegistry.registerModEntity(Traders.getResource(name), entityClass, 
				name, InitEntity.nextId(), Traders.getInstance(), 64, 1, true, 
				primaryEggColor, secondaryEggColor
		);
	}
	
	private static void registerEntityRenderer(Class<? extends Entity> entityClass, IRenderFactory renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
	}
	
	public static void registerEntityRenderers() {
		registerEntityRenderer(EntityTom.class, RenderTrader.getRenderFactory());
		registerEntityRenderer(EntityBobby.class, RenderTrader.getRenderFactory());
	}
	
}
