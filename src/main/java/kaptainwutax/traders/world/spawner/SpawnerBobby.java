package kaptainwutax.traders.world.spawner;

import java.util.List;
import java.util.function.Predicate;

import kaptainwutax.traders.entity.EntityBobby;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitSoundEvent;
import kaptainwutax.traders.util.ServerChat;
import kaptainwutax.traders.util.Time;
import kaptainwutax.traders.world.data.WorldDataTime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpawnerBobby extends Spawner {

	private static final Predicate<World> WORLD_PREDICATE = world -> {return world.provider.getDimension() == 0;};
	
	public SpawnerBobby() {
		super(12000, WORLD_PREDICATE);
	}

	@Override
	protected List<EntityPlayer> getPlayers(List<EntityPlayer> players) {
		return players;
	}
	
	private void postSpawn(EntityTrader trader, EntityPlayer player) {
		for(double x = 0.0d; x < 0.8d; x += 0.1d) {
			for(double z = 0.0d; z < 0.8d; z += 0.1d) {
				for(double y = 0.0d; y < 1.8d; y += 0.1d) {
					((WorldServer)trader.world).spawnParticle(EnumParticleTypes.PORTAL, trader.posX + x, trader.posY + y, trader.posZ + z, 1, 0.0d, 0.2d, 0.0d, 0);
				}
			}
		}
		
		WorldDataTime data = WorldDataTime.get(trader.world);
		Time time = data.getTime();
		trader.spawnTime = time.getTime();
		
		trader.world.playSound(null, trader.getPosition(), InitSoundEvent.VILLAGER_VANISH, SoundCategory.PLAYERS, 4.0f, 3.2f);
		ServerChat.whisper((EntityPlayerMP)player, trader, InitConfig.CONFIG_BOBBY.GREETING_PHRASES);
	}

	@Override
	protected void spawn(World world, List<EntityPlayer> players) {
		Time time = WorldDataTime.get(world).getTime();

		//Wednesday
		if(time.DAY_OF_WEEK != 3 || time.getTime() % 24000 != 12000)return;		
		
		for(EntityPlayer player: players) {
			for(int i = 0; i < 5; i++) {
				int xo = world.rand.nextInt(24) + 12;	
				int zo = world.rand.nextInt(24) + 12;		
				xo *= (world.rand.nextBoolean()) ? 1 : -1;
				zo *= (world.rand.nextBoolean()) ? 1 : -1;	
				
				int x = player.getPosition().getX() + xo;	
				int z = player.getPosition().getZ() + zo;		
				
				int y = world.getHeight(x, z);
				
				EntityTrader trader = new EntityBobby(world);
				trader.setPosition(x, y, z);
				
				if(trader.getCanSpawnHere()) {		
					world.spawnEntity(trader);
					trader.onGround = true;
					trader.getNavigator().tryMoveToEntityLiving(player, 1.0f);
					this.postSpawn(trader, player);
					break;
				} 
			}
		}
	}

}
