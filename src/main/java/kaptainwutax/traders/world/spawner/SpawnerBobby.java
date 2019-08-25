package kaptainwutax.traders.world.spawner;

import java.util.Random;
import java.util.function.Predicate;

import kaptainwutax.traders.entity.EntityBobby;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.init.InitSoundEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpawnerBobby extends Spawner {

	private static final Predicate<World> WORLD_PREDICATE = world -> {return world.provider.getDimension() == 0;};
		
	public SpawnerBobby(int delay) {
		super(delay, WORLD_PREDICATE);
	}

	@Override
	protected void spawn(World world, EntityPlayer player) {
		for(int i = 0; i < 5; i++) {
			int x = player.getPosition().getX() + world.rand.nextInt(24) + 12;	
			int z = player.getPosition().getZ() + world.rand.nextInt(24) + 12;		
			x *= (world.rand.nextInt(2) - 1);
			z *= (world.rand.nextInt(2) - 1);			
			int y = world.getHeight(x, z);
			
			EntityTrader trader = new EntityBobby(world);
			trader.setPosition(x, y, z);
			
			if(trader.getCanSpawnHere()) {		
				world.spawnEntity(trader);	
				this.postSpawn(trader);
				break;
			} 
		}
	}

	private void postSpawn(EntityTrader trader) {
		for(double x = 0.0d; x < 0.6d; x += 0.1d) {
			for(double z = 0.0d; z < 0.6d; z += 0.1d) {
				for(double y = 0.0d; y < 1.8d; y += 0.1d) {
					((WorldServer)trader.world).spawnParticle(EnumParticleTypes.PORTAL, trader.posX + x, trader.posY + y, trader.posZ + z, 1, 0.0d, 0.2d, 0.0d, 0);
				}
			}
		}
		
		trader.world.playSound(null, trader.getPosition(), InitSoundEvent.VILLAGER_VANISH, SoundCategory.PLAYERS, 2.0f, 3.2f);
	}

}
