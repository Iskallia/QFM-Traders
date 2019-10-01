package kaptainwutax.traders.event;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.container.ContainerVillager;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.util.NpcTrader;
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
		
		final String uuid = merchant.getDisplayName().getStyle().getHoverEvent().getValue().getFormattedText().split("\"")[3];
		
		if(!EntityTrader.UUIDS.contains(uuid))return;
		
		event.getEntityPlayer().openContainer = new ContainerVillager(player.inventory, 
				new NpcTrader(player, merchant.getDisplayName(), (EntityTrader)player.world.getLoadedEntityList().stream().filter(e -> e.getUniqueID().toString().equals(uuid)).collect(Collectors.toList()).get(0)), player.world);
		player.openContainer.addListener((EntityPlayerMP)player);
	}
	
}
