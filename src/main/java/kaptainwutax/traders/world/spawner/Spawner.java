package kaptainwutax.traders.world.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import kaptainwutax.traders.util.Time;
import kaptainwutax.traders.world.data.WorldDataTime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class Spawner {
	
	public static final Random RAND = new Random();
	
	private long lastAttempt = -1;
	
	private int delay;
	private Predicate<World> worldPredicate;
	
	public Spawner(int delay, Predicate<World> worldPredicate) {
		this.delay = delay;
		this.worldPredicate = worldPredicate;
	}
	
	protected List<EntityPlayer> getPlayers(List<EntityPlayer> players) {
		List<EntityPlayer> spawnPlayers = new ArrayList<EntityPlayer>();
		spawnPlayers.add(players.get(players.size()));
		return spawnPlayers;	
	}
	
	protected abstract void spawn(World world, List<EntityPlayer> list);

	public void tick(World world) {
		if(world.isRemote || !this.worldPredicate.test(world) || world.getTotalWorldTime() == this.lastAttempt)return;
		
		Time time = WorldDataTime.get(world).getTime();
		
		if(time.getTime() % this.delay == 0) {
			List<EntityPlayer> players = world.playerEntities.stream().filter(player -> !player.isSpectator()).collect(Collectors.toList());
			if(players.size() > 0) {
				this.spawn(world, this.getPlayers(players));
			}
		}
		
		lastAttempt = world.getTotalWorldTime();
	}
	
}
