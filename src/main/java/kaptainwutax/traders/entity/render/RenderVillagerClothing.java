package kaptainwutax.traders.entity.render;

import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.property.PTomColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
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
		
		if(trader instanceof EntityTom) {
			EntityTom tom = (EntityTom)trader;
			
			for(int i = 1; i < this.layers.length; i++) {	
				ResourceLocation layer = this.layers[i];
				
				PTomColor color = (i == 1 ? tom.favouriteColor : tom.favouriteColor2);
				EnumDyeColor dyeColor = EnumDyeColor.byMetadata(color.colorId);
				float[] values = dyeColor.getColorComponentValues();

				GlStateManager.color(values[0], values[1], values[2]);
				this.traderRenderer.bindTexture(layer);
				ModelTrader model = (ModelTrader)this.traderRenderer.getMainModel();
				model.render(trader, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);	  
				GlStateManager.resetColor();
			}	
			
			return;
		}
		
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
