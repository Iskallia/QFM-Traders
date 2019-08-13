package kaptainwutax.traders.handler;

import kaptainwutax.traders.container.ContainerTrader;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.gui.GuiContainerTrader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class HandlerGui implements IGuiHandler {

	public static final int TRADER = 0;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID(x);
		
		switch(id) {
			case TRADER: 
				if(entity instanceof EntityTrader) {			
					return new ContainerTrader(player.inventory, (EntityTrader)entity);
				}
				
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID(x);
		
		switch(id) {
			case TRADER: 
				if(entity instanceof EntityTrader) {			
					return new GuiContainerTrader(player.inventory, (EntityTrader)entity);
				}
				
			default:
				return null;
		}
	}

}
