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

public class EntityBobby extends EntityTrader {

	public EntityBobby(World world) {
		super(world, "Bobby");
		
		List<String> names = InitConfig.CONFIG_BOBBY.NAMES;
		
		if(!this.world.isRemote && names.size() > 0) {
			String name = names.get(this.rand.nextInt(names.size()));
			this.setCustomNameTag(name);
		}
	}

	@Override
	public List<Trade> getNewTrades() {
		List<Trade> randomTrades = new ArrayList<Trade>(InitTrade.BOBBY);
		Collections.shuffle(randomTrades);	
		
		randomTrades = randomTrades.stream()
				.limit(InitConfig.CONFIG_BOBBY.TRADES_COUNT)
				.collect(Collectors.toList());
		
		return randomTrades;
	}

	@Override
	public ResourceLocation[] getLayers() {
		return new ResourceLocation[] {
				Traders.getResource("textures/entity/trader/bobby_1.png")
		};
	}

}
