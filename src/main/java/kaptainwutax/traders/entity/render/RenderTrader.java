package kaptainwutax.traders.entity.render;

import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTrader extends RenderVillager {

	protected static IRenderFactory renderFactory = new Factory();
	
	public static IRenderFactory getRenderFactory() {
		return RenderTrader.renderFactory;
	}
	
	public RenderTrader(RenderManager renderManager) {
		super(renderManager);
	}
	
	public static class Factory implements IRenderFactory<EntityTrader> {
		@Override
		public Render<? super EntityTrader> createRenderFor(RenderManager manager) {
			return new RenderTrader(manager);
		}		
	}

}
