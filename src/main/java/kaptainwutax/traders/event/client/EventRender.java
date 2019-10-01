package kaptainwutax.traders.event.client;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.property.PTomBlocks;
import kaptainwutax.traders.entity.property.PTomColor;
import kaptainwutax.traders.entity.property.PTomPet;
import kaptainwutax.traders.util.Product;
import kaptainwutax.traders.util.Time;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Traders.MOD_ID, value = Side.CLIENT)
public class EventRender {
	
	private static Minecraft MINECRAFT = Minecraft.getMinecraft();
	
	public static void drawString(String text, int color, int x, int y) {
		FontRenderer fontRender = MINECRAFT.fontRenderer;
		ScaledResolution scaled = new ScaledResolution(MINECRAFT);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();	

		int stringWidth = fontRender.getStringWidth(text);		
		
		GL11.glPushMatrix();
		fontRender.drawStringWithShadow(text, x, y, color);
		GL11.glPopMatrix();		
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent event) {	
		if(event.getType() != ElementType.SUBTITLES)return;
		renderTimeBar();
		renderTomHappinessBar();
		renderTomHealthAndFood();
		renderTomProperties();
	}	
	
	private static void renderTomProperties() {
		RayTraceResult hit = MINECRAFT.objectMouseOver;
		if(hit == null)return;
		if(hit.typeOfHit != RayTraceResult.Type.ENTITY)return;
		if(hit.entityHit == null)return;
		if(!(hit.entityHit instanceof EntityTom))return;
		if(MINECRAFT.ingameGUI == null)return;
		
		EntityTom tom = (EntityTom)hit.entityHit;
		
		if(tom == null)return;
		
		PTomBlocks b = tom.favouriteBlocks;
		PTomPet p = tom.favouritePet;
		PTomColor c = tom.favouriteColor;
		PTomColor c2 = tom.favouriteColor2;
		
		RenderItem itemRender = MINECRAFT.getRenderItem();
		FontRenderer fontRenderer = MINECRAFT.fontRenderer;
		
		ScaledResolution sr = new ScaledResolution(MINECRAFT);
		int width = sr.getScaledWidth();
		int height = sr.getScaledHeight();
		int startY = 50;
		int startX = width - 30;
		int itemSize = 24;
		
		RenderHelper.enableGUIStandardItemLighting();
		
		int count;
		
		int renderI = 0;
		
		for(int i = 0; i < b.blocks.size(); i++) {
			Product product = b.products.get(i);				
			ItemStack blockStack = new ItemStack(product.getItem(), b.counts.get(i), product.getMetadata());
			itemRender.renderItemAndEffectIntoGUI(blockStack, startX - renderI * itemSize, startY);
			itemRender.renderItemOverlays(fontRenderer, blockStack, startX - renderI * itemSize, startY);	
			if(b.counts.get(i) != 0)renderI++;
		}
		
		ItemStack colorStack = new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE), c.coloredBlocksCount, c.colorId);
		itemRender.renderItemAndEffectIntoGUI(colorStack, startX, startY + itemSize);
		itemRender.renderItemOverlays(fontRenderer, colorStack, startX, startY + itemSize);		
		
		ItemStack colorStack2 = new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE), c2.coloredBlocksCount, c2.colorId);
		itemRender.renderItemAndEffectIntoGUI(colorStack2, startX - itemSize, startY + itemSize);
		itemRender.renderItemOverlays(fontRenderer, colorStack2, startX - itemSize, startY + itemSize);
		
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound entityTag = new NBTTagCompound();
		entityTag.setString("id", p.entityId);
		nbt.setTag("EntityTag", entityTag);
		ItemStack petStack = new ItemStack(Items.SPAWN_EGG, p.getEntityCount());
		petStack.setTagCompound(nbt);
		itemRender.renderItemAndEffectIntoGUI(petStack, startX - itemSize * 2, startY + itemSize);
		itemRender.renderItemOverlays(fontRenderer, petStack, startX - itemSize * 2, startY + itemSize);
		
		RenderHelper.disableStandardItemLighting();
	}

	public static void renderTimeBar() {		
		Time time = Time.getClient();
		boolean debug = false;
		
		String displayString = Time.DAY_NAMES[time.DAY_OF_WEEK] + " " 
				+ time.HOUR_OF_DAY +
				(time.MINUTE_OF_HOUR < 10 ? ":0" : ":") + time.MINUTE_OF_HOUR 
				+ " (Week " + time.WEEK + ")";
		
		if(debug)displayString += " " + time.getTime() % 24000;
		
		if(MINECRAFT.gameSettings.keyBindPlayerList.isKeyDown()) {
			drawString(displayString, Color.WHITE.getRGB(), 10, 10);
		}
	}
	
	private static void renderTomHappinessBar() {
		RayTraceResult hit = MINECRAFT.objectMouseOver;
		if(hit == null)return;
		if(hit.typeOfHit != RayTraceResult.Type.ENTITY)return;
		if(hit.entityHit == null)return;
		if(!(hit.entityHit instanceof EntityTom))return;
		if(MINECRAFT.ingameGUI == null)return;
		
		EntityTom tom = (EntityTom)hit.entityHit;
		
		if(tom == null)return;
		
		MINECRAFT.getTextureManager().bindTexture(Traders.getResource("textures/main.png"));
		ScaledResolution sr = new ScaledResolution(MINECRAFT);
		int width = sr.getScaledWidth();
		int height = sr.getScaledHeight();
		int startY = 10;
		int startX = width - 210;

		MINECRAFT.ingameGUI.drawTexturedModalRect(startX + 18, startY - 3, 0, 0, 182, 22);

		if(tom.getHappiness() >= 80) {
			GlStateManager.color(0.0f, 1.0f, 0.0f);
		} else if(tom.getHappiness() >= 60) {
			GlStateManager.color(1.0f, 1.0f, 0.0f);
		} else if(tom.getHappiness() >= 30) {
			GlStateManager.color(1.0f, 0.5f, 0.0f);
		} else if(tom.getHappiness() >= 0) {
			GlStateManager.color(1.0f, 0.0f, 0.0f);
		}
		
		MINECRAFT.ingameGUI.drawTexturedModalRect(startX + 18 + 3, startY, 0, 22, (int)((176.0f * (tom.getHappinessAnim() / tom.getMaxHappiness()))), 16);
		GlStateManager.resetColor();
		
		String text = "Happiness: " + tom.getHappiness() + "/" + tom.getMaxHappiness();
		MINECRAFT.ingameGUI.drawCenteredString(MINECRAFT.fontRenderer, text, startX + 111, startY + 4, 0x000000);
		MINECRAFT.ingameGUI.drawCenteredString(MINECRAFT.fontRenderer, text, startX + 111, startY + 4, 0xFFFFFF);
	}	
	
	public static void renderTomHealthAndFood() {
		RayTraceResult hit = MINECRAFT.objectMouseOver;
		if(hit == null)return;
		if(hit.typeOfHit != RayTraceResult.Type.ENTITY)return;
		if(hit.entityHit == null)return;
		if(!(hit.entityHit instanceof EntityTom))return;
		if(MINECRAFT.ingameGUI == null)return;
		
		EntityTom tom = (EntityTom)hit.entityHit;
		
		if(tom == null)return;
		
		GuiIngame gui = MINECRAFT.ingameGUI;
		MINECRAFT.getTextureManager().bindTexture(GuiIngame.ICONS);
		ScaledResolution scaledRes = new ScaledResolution(MINECRAFT);
		int i = MathHelper.ceil(tom.getHealth());
        FoodStats foodstats = tom.getFoodStats();
        int k = foodstats.getFoodLevel();
        int l = scaledRes.getScaledWidth() / 2 - 91;
        int i1 = scaledRes.getScaledWidth() / 2 + 91;
        int j1 = scaledRes.getScaledHeight() - 39;
        IAttributeInstance iattributeinstance = tom.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        float f = (float)iattributeinstance.getAttributeValue();
        int k1 = MathHelper.ceil(tom.getAbsorptionAmount());
        int l1 = MathHelper.ceil((f + (float)k1) / 2.0F / 10.0F);
        int i2 = Math.max(10 - (l1 - 2), 3);
        int j2 = j1 - (l1 - 1) * i2 - 10;
        int k2 = j1 - 10;
        int l2 = k1;
        int i3 = tom.getTotalArmorValue();
        int j3 = -1;
        
		for(int j5 = MathHelper.ceil((f + (float)k1) / 2.0F) - 1; j5 >= 0; --j5) {
            int healthBarColor = 16;

            if(tom.isPotionActive(MobEffects.POISON)) {
                healthBarColor += 36;
            } else if(tom.isPotionActive(MobEffects.WITHER)) {
                healthBarColor += 72;
            }

            int healthHighlight = 0;

            int dmg = tom.maxHurtResistantTime - tom.hurtResistantTime;
            
            if(dmg > 0 && dmg < 4) {
                healthHighlight = 1;
            }

            int j4 = MathHelper.ceil((float)(j5 + 1) / 10.0F) - 1;
            int k4 = l + j5 % 10 * 8;
          
            int x = (scaledRes.getScaledWidth() + j5 % 10 * 8) - 185;
            int y = 32;
            
            if (i <= 4) {
                y += tom.getRNG().nextInt(2);
            }

            if (l2 <= 0 && j5 == j3) {
                y -= 2;
            }            

            int i5 = 0;

            if(tom.world.getWorldInfo().isHardcoreModeEnabled()) {
                i5 = 5;
            }

            gui.drawTexturedModalRect(x, y, 16 + healthHighlight * 9, 9 * i5, 9, 9);

            if (l2 > 0) {
                if (l2 == k1 && k1 % 2 == 1) {
                	gui.drawTexturedModalRect(x, y, healthBarColor + 153, 9 * i5, 9, 9);
                    --l2;
                } else {
                	gui.drawTexturedModalRect(x, y, healthBarColor + 144, 9 * i5, 9, 9);
                    l2 -= 2;
                }
            } else {
                if (j5 * 2 + 1 < i) {
                	gui.drawTexturedModalRect(x, y, healthBarColor + 36, 9 * i5, 9, 9);
                }

                if (j5 * 2 + 1 == i) {
                	gui.drawTexturedModalRect(x, y, healthBarColor + 45, 9 * i5, 9, 9);
                }
            }
        }

		//FOOD
        for(int l5 = 0; l5 < 10; ++l5) {
            int x = (scaledRes.getScaledWidth() - l5 % 10 * 8) - 20;
            int y = 32;
            
            int j6 = j1;
            int l6 = 16;
            int j7 = 0;

            if(tom.isPotionActive(MobEffects.HUNGER)) {
                l6 += 36;
                j7 = 13;
            }

            int l7 = i1 - l5 * 8 - 9;
            gui.drawTexturedModalRect(x, y, 16 + j7 * 9, 27, 9, 9);

            if(l5 * 2 + 1 < k) {
             	gui.drawTexturedModalRect(x, y, l6 + 36, 27, 9, 9);
            }

            if(l5 * 2 + 1 == k) {
            	gui.drawTexturedModalRect(x, y, l6 + 45, 27, 9, 9);
            }
        }
		
	}
	
}
