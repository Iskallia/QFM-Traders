package kaptainwutax.traders.entity;

import kaptainwutax.traders.init.InitTrade;
import net.minecraft.world.World;

public class EntityBobby extends EntityTrader {

	public EntityBobby(World world) {
		super(world, "Bobby", InitTrade.BOBBY);
	}

}
