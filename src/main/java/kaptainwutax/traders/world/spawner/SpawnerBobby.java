package kaptainwutax.traders.world.spawner;

import java.util.function.Predicate;

import kaptainwutax.traders.entity.EntityBobby;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.world.World;

public class SpawnerBobby extends Spawner {

	private static final Predicate<World> WORLD_PREDICATE = world -> {return world.provider.getDimension() == 0;};
		
	public SpawnerBobby(int delay) {
		super(delay, WORLD_PREDICATE);
	}

	@Override
	protected void spawn(World world, EntityPlayer player) {
		for(int i = 0; i < 5; i++) {
			int x = player.getPosition().getX() + world.rand.nextInt(32) * (world.rand.nextInt(2) - 1);	
			int z = player.getPosition().getZ() + world.rand.nextInt(32) * (world.rand.nextInt(2) - 1);				
			int y = world.getHeight(x, z);
			
			EntityTrader trader = new EntityBobby(world);
			trader.setPosition(x, y, z);
			
			if(trader.getCanSpawnHere()) {		
				world.spawnEntity(trader);
				trader.onGround = true;
				
				if(trader.getNavigator().tryMoveToEntityLiving(player, 1.0f)) {
					break;
				} else {
					trader.setDead();
				}
			} 
		}
	}

}
