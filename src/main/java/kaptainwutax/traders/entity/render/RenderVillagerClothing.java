package kaptainwutax.traders.entity.render;

import kaptainwutax.traders.Traders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderVillagerClothing implements LayerRenderer<EntityLivingBase> {

    private RenderTrader traderRenderer;
    private ResourceLocation[] layers = new ResourceLocation[0];
    
    public RenderVillagerClothing(RenderTrader traderRenderer) {
        this.traderRenderer = traderRenderer;
    }
    
	@Override
	public void doRenderLayer(EntityLivingBase trader, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(trader.isInvisible())return;

		boolean main = true;
		
		for(ResourceLocation layer: this.layers) {
			if(main) {
				main = false;
				continue;
			}
			
			this.traderRenderer.bindTexture(layer);
			ModelTrader model = (ModelTrader)this.traderRenderer.getMainModel();
			model.render(trader, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);	        
		}	
	}	

	public ResourceLocation[] getLayers() {
		return this.layers;
	}
	
	public void setLayers(ResourceLocation[] layers) {
		this.layers = layers;
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
