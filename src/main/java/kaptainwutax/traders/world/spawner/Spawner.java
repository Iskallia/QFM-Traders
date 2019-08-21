package kaptainwutax.traders.world.spawner;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import kaptainwutax.traders.util.Time;
import kaptainwutax.traders.world.data.WorldDataTime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class Spawner {
	
	private World world;
	private int delay;
	
	public Spawner(World world, int delay, Predicate<World> worldPredicate) {
		this.world = world;
		this.delay = delay;
	}
	
	public void tick() {
		if(this.world.isRemote)return;
		
		Time time = WorldDataTime.get(this.world).getTime();
		
		if(time.getTime() % this.delay == 0) {
			List<EntityPlayer> players = this.world.playerEntities.stream().filter(player -> !player.isSpectator()).collect(Collectors.toList());
			if(players.size() > 0)this.spawn(this.world, players.get(this.world.rand.nextInt(players.size())));
		}
	}

	protected abstract void spawn(World world, EntityPlayer player);
	
}
