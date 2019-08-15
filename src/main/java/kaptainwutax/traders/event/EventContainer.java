package kaptainwutax.traders.event;

import java.lang.reflect.Field;

import kaptainwutax.traders.Traders;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Traders.MOD_ID)
public class EventContainer {

	@SubscribeEvent
	public static void onOpenContainer(PlayerContainerEvent.Open event) {
		Container container = event.getContainer();
		System.out.println("Open!");
		if(!(container instanceof ContainerMerchant))return;
		
		ContainerMerchant merchantContainer = (ContainerMerchant)container;
		EntityPlayer player = event.getEntityPlayer();
		
		Field[] fields = merchantContainer.getClass().getDeclaredFields();

		for(Field f: fields) {
			System.out.println(f.getName());
		}
		
		//event.getEntityPlayer().openContainer = new ContainerVillager(player.inventory, merchantContainer , player.world);
	}
	
}
