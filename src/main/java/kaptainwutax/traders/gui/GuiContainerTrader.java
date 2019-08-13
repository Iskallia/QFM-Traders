package kaptainwutax.traders.gui;

import java.awt.Color;

import kaptainwutax.traders.container.ContainerTrader;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiContainerTrader extends GuiContainer {

	private static Minecraft MINECRAFT = Minecraft.getMinecraft();
	private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
	
	private EntityTrader trader;
	
	public GuiContainerTrader(InventoryPlayer playerInventory, EntityTrader trader) {
		super(new ContainerTrader(playerInventory, trader));
		this.trader = trader;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(trader.getCustomNameTag(), 5, 5, Color.DARK_GRAY.getRGB());
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		MINECRAFT.getTextureManager().bindTexture(INVENTORY_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

}
