package kaptainwutax.traders.gui;

import java.awt.Color;
import java.util.Iterator;

import org.lwjgl.input.Mouse;

import kaptainwutax.traders.container.ContainerTrader;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiContainerTrader extends GuiContainer {

	private static Minecraft MINECRAFT = Minecraft.getMinecraft();
	private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
    private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("qfm_traders", "textures/gui/container/villager2.png");

	private EntityTrader trader;
	private WidgetButtonPage[] buttons = new WidgetButtonPage[7];
	public int slotOffset = 0;
	private int blitOffset;
	
	public GuiContainerTrader(InventoryPlayer playerInventory, EntityTrader trader) {
		super(new ContainerTrader(playerInventory, trader));
		this.trader = trader;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void initGui() {
		super.initGui();
		
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        int int_3 = j + 46;

        for(int int_4 = 0; int_4 < this.buttons.length; ++int_4) {
            this.buttons[int_4] = this.addButton(new WidgetButtonPage(int_4, i - 95, int_3));
            int_3 += 20;
         }
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		MerchantRecipeList merchantrecipelist = this.trader.getRecipes(this.mc.player);
		if(merchantrecipelist == null)return;
		
		int scroll = Mouse.getDWheel();

		while(scroll >= 120) {
			scroll -= 120;
			this.slotOffset--;
		}
		
		while(scroll <= -120) {
			scroll += 120;
			this.slotOffset++;
		}
		
		int upperLimit = MathHelper.clamp(merchantrecipelist.size() - 7, 0, merchantrecipelist.size() - 7);
		this.slotOffset = MathHelper.clamp(this.slotOffset, 0, upperLimit);
	}

	
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        
        MerchantRecipeList merchantrecipelist = this.trader.getRecipes(this.mc.player);

        if(merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
            int i = (this.width - 276) / 2;
            int j = (this.height - 166) / 2;
            int k = 1;

            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            
            this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
            this.method_20221(i, j, merchantrecipelist);
            int int_6 = i + 5 + 5 - 50;
            int int_5 = j + 16 + 1;
            int int_7 = 0;
            Iterator var10 = merchantrecipelist.iterator();

            MerchantRecipe tradeOffer_1;
           while(var10.hasNext()) {
              tradeOffer_1 = (MerchantRecipe)var10.next();
              if (this.method_20220(merchantrecipelist.size()) && (int_7 < this.slotOffset || int_7 >= 7 + this.slotOffset)) {
                 ++int_7;
              } else {
                 ItemStack itemStack_1 = tradeOffer_1.getItemToBuy();
                 ItemStack itemStack_2 = tradeOffer_1.getItemToBuy();
                 ItemStack itemStack_3 = tradeOffer_1.getSecondItemToBuy();
                 ItemStack itemStack_4 = tradeOffer_1.getItemToSell();
                 this.itemRender.zLevel = 100.0F;
                 int int_8 = int_5 + 2;
                 
                 //Buy Items.
                 this.method_20222(itemStack_2, itemStack_1, int_6, int_8);
                 if (!itemStack_3.isEmpty()) {
                    this.itemRender.renderItemAndEffectIntoGUI(itemStack_3, i + 5 + 35 - 50, int_8);
                    this.itemRender.renderItemOverlays(this.fontRenderer, itemStack_3, i + 5 + 35 - 50, int_8);
                 }

                 //Arrows.
                 this.method_20223(tradeOffer_1, int_6 - 10, int_8);
                 this.itemRender.renderItemAndEffectIntoGUI(itemStack_4, i + 5 + 68 - 50, int_8);
                 this.itemRender.renderItemOverlays(this.fontRenderer, itemStack_4, i + 5 + 68 - 50, int_8);
                 this.itemRender.zLevel = 0.0F;
                 int_5 += 20;
                 ++int_7;
              }
           }
           
            WidgetButtonPage[] var18 = this.buttons;
            int var19 = var18.length;

            for(int var20 = 0; var20 < var19; ++var20) {
               WidgetButtonPage merchantScreen$WidgetButtonPage_1 = var18[var20];
            	
               if (merchantScreen$WidgetButtonPage_1.isMouseOver()) {           	  
                  merchantScreen$WidgetButtonPage_1.renderToolTip(mouseX, mouseY);
               }

               merchantScreen$WidgetButtonPage_1.visible = merchantScreen$WidgetButtonPage_1.id < merchantrecipelist.size();
            }

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }

        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
    private void method_20223(MerchantRecipe tradeOffer_1, int int_1, int int_2) {
    	RenderHelper.disableStandardItemLighting();   	
        GlStateManager.enableBlend();
        
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        if (tradeOffer_1.isRecipeDisabled()) {
           blit(int_1 + 5 + 35 + 20, int_2 + 3, 100, 25.0F, 171.0F, 10, 9, 256, 512);
        } else {
           blit(int_1 + 5 + 35 + 20, int_2 + 3, 100, 15.0F, 171.0F, 10, 9, 256, 512);          
        }

        RenderHelper.enableGUIStandardItemLighting();
     }
    
    private boolean method_20220(int int_1) {
        return int_1 > 7;
     }
    
    private void method_20221(int int_1, int int_2, MerchantRecipeList traderOfferList_1) {
    	RenderHelper.disableStandardItemLighting();
        int int_3 = traderOfferList_1.size() + 1 - 7;
        if (int_3 > 1) {
           int int_4 = 139 - (27 + (int_3 - 1) * 139 / int_3);
           int int_5 = 1 + int_4 / int_3 + 139 / int_3;
           int int_7 = Math.min(113, this.slotOffset * int_5);
           if (this.slotOffset == int_3 - 1) {
              int_7 = 113;
           }

           blit(int_1 + 94 - 50, int_2 + 18 + int_7, 100, 0.0F, 199.0F, 6, 27, 256, 512);
        } else {
           blit(int_1 + 94 - 50, int_2 + 18, 100, 6.0F, 199.0F, 6, 27, 256, 512);
        }

        RenderHelper.enableGUIStandardItemLighting();
     }
    
    private void method_20222(ItemStack itemStack_1, ItemStack itemStack_2, int int_1, int int_2) {
        this.itemRender.renderItemAndEffectIntoGUI(itemStack_1, int_1, int_2);
        if (itemStack_2.getCount() == itemStack_1.getCount()) {
           this.itemRender.renderItemOverlays(this.fontRenderer, itemStack_1, int_1, int_2);
        } else {
           this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack_2, int_1, int_2, itemStack_2.getCount() == 1 ? "1" : null);
           this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack_1, int_1 + 14, int_2, itemStack_1.getCount() == 1 ? "1" : null);
           this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
           this.blitOffset += 300;
           RenderHelper.disableStandardItemLighting();
           blit(int_1 + 7, int_2 + 12, this.blitOffset, 0.0F, 176.0F, 9, 2, 256, 512);
           RenderHelper.enableGUIStandardItemLighting();
           this.blitOffset -= 300;
        }

     }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(trader.getCustomNameTag(), 5, 5, Color.DARK_GRAY.getRGB());
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        
		this.drawDefaultBackground();
		this.zLevel = 100;
		
		MINECRAFT.getTextureManager().bindTexture(INVENTORY_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);	
		
		this.zLevel--;
		
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);               
        MINECRAFT.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        int i = (this.width - 276) / 2;
        int j = (this.height - 166) / 2;
        this.blit(i - 50, j, (int)this.zLevel, 0.0F, 0.0F, 276, 166, 256, 512);
	}
	
    private void blit(int int_1, int int_2, int int_3, float float_1, float float_2, int int_4, int int_5, int int_6, int int_7) {
    	innerBlit(int_1, int_1 + int_4, int_2, int_2 + int_5, int_3, int_4, int_5, float_1, float_2, int_7, int_6);	
	}

    private void innerBlit(int int_1, int int_2, int int_3, int int_4, int int_5, int int_6, int int_7, float float_1, float float_2, int int_8, int int_9) {
        innerBlit(int_1, int_2, int_3, int_4, int_5, (float_1 + 0.0F) / int_8, (float_1 + int_6) / int_8, (float_2 + 0.0F) / int_9, (float_2 + int_7) / int_9);
    }
    
    protected void innerBlit(int int_1, int int_2, int int_3, int int_4, int int_5, float float_1, float float_2, float float_3, float float_4) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(int_1, int_4, int_5).tex(float_1, float_4).endVertex();
        bufferbuilder.pos(int_2, int_4, int_5).tex(float_2, float_4).endVertex();
        bufferbuilder.pos(int_2, int_3, int_5).tex(float_2, float_3).endVertex();
        bufferbuilder.pos(int_1, int_3, int_5).tex(float_1, float_3).endVertex();
        tessellator.draw();
     }
    
	@SideOnly(Side.CLIENT)
	class WidgetButtonPage extends GuiButton {
      final int id;

      public WidgetButtonPage(int id, int x, int y) {
         super(id, x, y, 89, 20, "");
         this.id = id;
         this.visible = false;
      }

      public int getId() {
         return this.id;
      }
      
      public void renderToolTip(int int_1, int int_2) {
    	  MerchantRecipeList recipes = GuiContainerTrader.this.trader.getRecipes(GuiContainerTrader.this.mc.player);
    	  if(recipes == null)return;

    	  MerchantRecipe recipe = recipes.get(this.id + GuiContainerTrader.this.slotOffset);
    	  
         if (this.hovered && recipes.size() > this.id + GuiContainerTrader.this.slotOffset) {
            ItemStack itemStack_3;
            if (int_1 < this.x + 20) {
               itemStack_3 = recipe.getItemToBuy();
               GuiContainerTrader.this.renderToolTip(itemStack_3, int_1, int_2);
            } else if (int_1 < this.x + 50 && int_1 > this.x + 30) {
               itemStack_3 = recipe.getSecondItemToBuy();
               if (!itemStack_3.isEmpty()) {
            	   GuiContainerTrader.this.renderToolTip(itemStack_3, int_1, int_2);
               }
            } else if (int_1 > this.x + 65) {
               itemStack_3 = recipe.getItemToSell();
               GuiContainerTrader.this.renderToolTip(itemStack_3, int_1, int_2);
            }
         }

      }		
	}

}
