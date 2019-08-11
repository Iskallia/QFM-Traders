package kaptainwutax.traders.init;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.entity.render.RenderTrader;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class InitEntity {

	private static int ID = 0;

	public static void registerEntities() {
		InitEntity.registerEntityAndEgg("test_trader", EntityTrader.class, 0xFFFFFF, 0x000000);
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
	
	public static void registerEntityRenderer(Class<? extends Entity> entityClass, IRenderFactory renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
	}
	
	private static int nextId() {
		return InitEntity.ID++;
	}
	
}
