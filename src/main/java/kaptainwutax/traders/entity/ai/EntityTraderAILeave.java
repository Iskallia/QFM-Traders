package kaptainwutax.traders.entity.ai;

import java.util.Random;

import kaptainwutax.traders.entity.EntityBobby;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitSoundEvent;
import kaptainwutax.traders.world.data.WorldDataTime;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityTraderAILeave extends EntityAIBase {

	private EntityBobby bobby;
	private int runTime;
	
	public EntityTraderAILeave(EntityBobby bobby) {
		this.bobby = bobby;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return true;
	}
	
	@Override
	public boolean shouldExecute() {
		World world = this.bobby.world;
		WorldDataTime data = WorldDataTime.get(world);
		long time = data.getTime().getTime();
		long ticksExisted = time - this.bobby.spawnTime;
		return this.bobby.spawnTime >= 0 && (ticksExisted < 0 || ticksExisted >= InitConfig.CONFIG_BOBBY.DESPAWN_DELAY);
	}
	
	@Override
	public void startExecuting() {
		this.runTime = 0;
	}
	
	@Override
	public void updateTask() {		
		super.updateTask();

		if(this.runTime == 40) {
			this.bobby.world.playSound(null, this.bobby.getPosition(), InitSoundEvent.VILLAGER_VANISH, SoundCategory.PLAYERS, 5.0f, 1.0f - new Random().nextFloat() * 0.1f);
		}
		
		if(this.runTime == 60) {					
			for(double x = 0; x < 0.8; x += 0.1) {
				for(double z = 0; z < 0.8; z += 0.1) {
					for(double y = 0; y < 1.8; y += 0.1) {
						((WorldServer)this.bobby.world).spawnParticle(EnumParticleTypes.PORTAL, this.bobby.posX + x, this.bobby.posY + y, this.bobby.posZ + z, 1, 0.0d, 0.2d, 0.0d, 0);
					}
				}
			}
			
			((WorldServer)this.bobby.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.bobby.posX, this.bobby.posY + 1.2, this.bobby.posZ, 1, 0.0d, 0.2d, 0.0d, 0);			
			this.bobby.setDead();
		}
		
		this.bobby.noClip = true;
		this.bobby.setNoGravity(true);
		this.bobby.addVelocity((new Random().nextFloat() - 0.5f), 0.01f, (new Random().nextFloat() - 0.5f));
		((WorldServer)this.bobby.world).spawnParticle(EnumParticleTypes.PORTAL, this.bobby.posX, this.bobby.posY, this.bobby.posZ, 4, 0.0d, 0.2d, 0.0d, 0);
		this.runTime++;
	}
	
}
