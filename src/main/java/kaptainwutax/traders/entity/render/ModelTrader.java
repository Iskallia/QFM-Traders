package kaptainwutax.traders.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelTrader extends ModelVillager {

    protected ModelRenderer headOverlay;
    protected ModelRenderer hat;
    protected ModelRenderer robe;
    
    public ModelTrader(float scale) {
        this(scale, 0.0F, 64, 64);
    }

    public ModelTrader(float scale, float p_i1164_2_, int width, int height) {
    	super(scale, p_i1164_2_, width, height);
        this.headOverlay = (new ModelRenderer(this)).setTextureSize(width, height);
        this.headOverlay.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
        this.headOverlay.setTextureOffset(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, scale + 0.5F);
        
        this.hat = (new ModelRenderer(this)).setTextureSize(width, height);
        this.hat.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
        this.hat.setTextureOffset(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16, 16, 1, scale);
        this.hat.rotateAngleX = -1.5707964F; //pitch
        this.headOverlay.addChild(this.hat);
        
        this.robe = (new ModelRenderer(this)).setTextureSize(width, height);
        this.robe.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
        this.robe.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, scale + 0.5F);
        this.villagerBody.addChild(this.robe);
    }
    
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.headOverlay.render(scale);
        this.villagerHead.render(scale);
        this.villagerBody.render(scale);
        this.rightVillagerLeg.render(scale);
        this.leftVillagerLeg.render(scale);
        this.villagerArms.render(scale);
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) {
    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.headOverlay.rotateAngleY = netHeadYaw * 0.017453292F;
        this.headOverlay.rotateAngleX = headPitch * 0.017453292F;
    }

}
