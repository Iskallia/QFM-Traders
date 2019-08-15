package kaptainwutax.traders.event.client;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.gui.GuiContainerVillager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Traders.MOD_ID, value = Side.CLIENT)
public class EventGui {

	private static Minecraft MINECRAFT = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event) {
		GuiScreen gui = event.getGui();
		
		if(!(gui instanceof GuiMerchant))return;
		
		GuiScreen merchantGui = (GuiMerchant)gui;
		EntityPlayer player = MINECRAFT.player;

		event.setGui(new GuiContainerVillager(player.inventory, new NpcMerchant(player, null), player.world));
	}
	
}
