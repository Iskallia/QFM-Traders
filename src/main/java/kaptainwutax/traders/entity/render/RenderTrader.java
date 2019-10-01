package kaptainwutax.traders.entity.render;

import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTrader extends RenderLiving {
	
	public static class Factory implements IRenderFactory<EntityTrader> {
		@Override
		public Render<? super EntityTrader> createRenderFor(RenderManager manager) {
			return new RenderTrader(manager);
		}		
	}
	protected static IRenderFactory renderFactory = new Factory();
	
	public static IRenderFactory getRenderFactory() {
		return RenderTrader.renderFactory;
	}
	
	protected RenderVillagerClothing clothingRenderer = new RenderVillagerClothing(this);

	public RenderTrader(RenderManager renderManager) {
		super(renderManager, new ModelTrader(0.0f), 0.5f);
		this.addLayer(clothingRenderer);
	}    
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		this.clothingRenderer.setLayers(((ILayeredTextures)entity).getLayers());
		return this.clothingRenderer.getLayers()[0];
	}  

}
