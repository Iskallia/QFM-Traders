package kaptainwutax.traders.event.client;

import java.lang.reflect.Field;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.gui.GuiContainerVillager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Traders.MOD_ID, value = Side.CLIENT)
public class EventGui {

	private static Minecraft MINECRAFT = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event) throws IllegalArgumentException, IllegalAccessException {
		GuiScreen gui = event.getGui();
		
		if(!(gui instanceof GuiMerchant))return;
		
		Field merchantField = gui.getClass().getDeclaredFields()[2];
		merchantField.setAccessible(true);
		IMerchant merchant = (IMerchant)merchantField.get(gui);
		
		String uuid = merchant.getDisplayName().getStyle().getHoverEvent().getValue().getFormattedText();
		uuid = uuid.split("\"")[3];
		
		if(!EntityTrader.UUIDS.contains(uuid))return;

		EntityPlayer player = MINECRAFT.player;
	
		event.setGui(new GuiContainerVillager(player.inventory, merchant, player.world));
	}
	
}
