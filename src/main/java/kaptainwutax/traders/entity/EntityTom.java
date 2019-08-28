package kaptainwutax.traders.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitTrade;
import kaptainwutax.traders.util.Trade;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityTom extends EntityTrader {

	public EntityTom(World world) {
		super(world, "Tom");
	}

	@Override
	public List<Trade> getNewTrades() {
		List<Trade> randomTrades = new ArrayList<Trade>(InitTrade.TOM);
		Collections.shuffle(randomTrades);	
		
		randomTrades = randomTrades.stream()
				.limit(InitConfig.CONFIG_TOM.TRADES_COUNT)
				.collect(Collectors.toList());
		
		return randomTrades;
	}

	@Override
	public ResourceLocation[] getLayers() {
		return new ResourceLocation[] {
				Traders.getResource("textures/entity/trader/tom_1.png"),
				Traders.getResource("textures/entity/trader/tom_2.png"),
				Traders.getResource("textures/entity/trader/tom_3.png")
		};
	}

}
