package kaptainwutax.traders.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Players {

	public static EntityPlayer getPlayerFromUUID(World world, String uuid) {
		return world.getPlayerEntityByUUID(UUID.fromString(uuid));
	}
	
}
