package kaptainwutax.traders.entity;

import java.util.List;

import kaptainwutax.traders.init.InitTrade;
import kaptainwutax.traders.util.Trade;
import net.minecraft.world.World;

public class EntityBobby extends EntityTrader {

	public EntityBobby(World world) {
		super(world, "Bobby");
	}

	@Override
	public List<Trade> getNewTrades() {
		return InitTrade.BOBBY;
	}

}
