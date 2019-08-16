package kaptainwutax.traders.event;

import java.lang.reflect.Field;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.container.ContainerVillager;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Traders.MOD_ID)
public class EventContainer {

	@SubscribeEvent
	public static void onOpenContainer(PlayerContainerEvent.Open event) throws IllegalArgumentException, IllegalAccessException {
		Container container = event.getContainer();

		if(event.getEntityPlayer().world.isRemote || !(container instanceof ContainerMerchant))return;
		
		ContainerMerchant merchantContainer = (ContainerMerchant)container;
		EntityPlayer player = event.getEntityPlayer();
		
		Field f = merchantContainer.getClass().getDeclaredFields()[0];
		f.setAccessible(true);
		IMerchant merchant = (IMerchant)f.get(merchantContainer);
		
		String uuid = merchant.getDisplayName().getStyle().getHoverEvent().getValue().getFormattedText();
		uuid = uuid.split("\"")[3];
		
		if(!EntityTrader.UUIDS.contains(uuid))return;
		
		event.getEntityPlayer().openContainer = new ContainerVillager(player.inventory, merchant, player.world);
		player.openContainer.addListener((EntityPlayerMP)player);
	}
	
}
