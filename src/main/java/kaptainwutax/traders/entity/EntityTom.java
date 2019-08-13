package kaptainwutax.traders.entity;

import kaptainwutax.traders.init.InitTrade;
import net.minecraft.world.World;

public class EntityTom extends EntityTrader {

	public EntityTom(World world) {
		super(world, "Tom", InitTrade.TOM);
	}

}
