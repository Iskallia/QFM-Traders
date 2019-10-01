package kaptainwutax.traders.init;

import java.util.ArrayList;
import java.util.List;

import kaptainwutax.traders.world.spawner.Spawner;
import kaptainwutax.traders.world.spawner.SpawnerBobby;

public class InitSpawner {

	public static List<Spawner> SPAWNERS = new ArrayList<Spawner>();
	
	public static Spawner BOBBY = new SpawnerBobby();
	
	private static void registerSpawner(Spawner spawner) {
		SPAWNERS.add(spawner);
	}
	
	public static void registerSpawners() {
		registerSpawner(BOBBY);
	}
	
}
