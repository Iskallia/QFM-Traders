package kaptainwutax.traders.gui;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import kaptainwutax.traders.container.ContainerTrader;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.gui.GuiContainerVillager.WidgetButtonPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
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
        int int_3 = j + 16 + 2;

        for(int int_4 = 0; int_4 < this.buttons.length; ++int_4) {
            this.buttons[int_4] = (WidgetButtonPage)this.addButton(new WidgetButtonPage(int_4, i + 5, int_3));
            int_3 += 20;
         }
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		MerchantRecipeList merchantrecipelist = this.trader.getRecipes(this.mc.player);
		
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
		MerchantRecipeList merchantrecipelist = this.trader.getRecipes(this.mc.player);
		
        WidgetButtonPage[] var18 = this.buttons;
        int var19 = var18.length;

        for(int var20 = 0; var20 < var19; ++var20) {
           WidgetButtonPage merchantScreen$WidgetButtonPage_1 = var18[var20];
        	
           if (merchantScreen$WidgetButtonPage_1.isMouseOver()) {           	  
              merchantScreen$WidgetButtonPage_1.renderToolTip(mouseX, mouseY);
           }

           merchantScreen$WidgetButtonPage_1.visible = merchantScreen$WidgetButtonPage_1.id < merchantrecipelist.size();
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

    private static void innerBlit(int int_1, int int_2, int int_3, int int_4, int int_5, int int_6, int int_7, float float_1, float float_2, int int_8, int int_9) {
        innerBlit(int_1, int_2, int_3, int_4, int_5, (float_1 + 0.0F) / (float)int_8, (float_1 + (float)int_6) / (float)int_8, (float_2 + 0.0F) / (float)int_9, (float_2 + (float)int_7) / (float)int_9);
    }
    
    protected static void innerBlit(int int_1, int int_2, int int_3, int int_4, int int_5, float float_1, float float_2, float float_3, float float_4) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)int_1, (double)int_4, (double)int_5).tex((double)float_1, (double)float_4).endVertex();
        bufferbuilder.pos((double)int_2, (double)int_4, (double)int_5).tex((double)float_2, (double)float_4).endVertex();
        bufferbuilder.pos((double)int_2, (double)int_3, (double)int_5).tex((double)float_2, (double)float_3).endVertex();
        bufferbuilder.pos((double)int_1, (double)int_3, (double)int_5).tex((double)float_1, (double)float_3).endVertex();
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
    	  MerchantRecipe recipe = recipes.get(this.id + GuiContainerTrader.this.slotOffset );
    	  
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
