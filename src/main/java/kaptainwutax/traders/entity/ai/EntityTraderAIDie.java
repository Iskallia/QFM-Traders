package kaptainwutax.traders.entity.ai;

import java.util.Random;
import java.util.UUID;

import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitSoundEvent;
import kaptainwutax.traders.util.ServerChat;
import kaptainwutax.traders.world.data.WorldDataTime;
import kaptainwutax.traders.world.data.WorldDataTomMap;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityTraderAIDie extends EntityAIBase {

	private EntityTom tom;
	private int runTime;
	
	public EntityTraderAIDie(EntityTom tom) {
		this.tom = tom;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return true;
	}
	
	@Override
	public boolean shouldExecute() {
		World world = this.tom.world;
		WorldDataTime data = WorldDataTime.get(world);
		long time = data.getTime().getTime();
		long ticksJobless = time - this.tom.joblessStart;
		
		boolean shouldExecute = ticksJobless < 0 || ticksJobless >= InitConfig.CONFIG_TOM.DESPAWN_DELAY;
		shouldExecute = shouldExecute && this.tom.joblessStart >= 0;

		if(shouldExecute) {
			WorldDataTomMap dataTom = WorldDataTomMap.get(world);
			String playerUUID = dataTom.getPlayerMap().get(this.tom.getUniqueID().toString());
			
			if(playerUUID != null) {
				EntityPlayer player = this.tom.world.getPlayerEntityByUUID(UUID.fromString(playerUUID));
				
				if(player != null) {
					WorldDataTime dataTime = WorldDataTime.get(world);
					this.tom.joblessStart = dataTime.getTime().getTime();
					ServerChat.whisper((EntityPlayerMP)player, this.tom, InitConfig.CONFIG_TOM.DESPAWN_PHRASES);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.runTime = 0;
	}
	
	@Override
	public void updateTask() {		
		super.updateTask();

		if(this.runTime == 40) {
			this.tom.world.playSound(null, this.tom.getPosition(), InitSoundEvent.VILLAGER_VANISH, SoundCategory.PLAYERS, 5.0f, 1.0f - new Random().nextFloat() * 0.1f);
		}
		
		if(this.runTime == 60) {					
			for(double x = 0; x < 0.8; x += 0.1) {
				for(double z = 0; z < 0.8; z += 0.1) {
					for(double y = 0; y < 1.8; y += 0.1) {
						((WorldServer)this.tom.world).spawnParticle(EnumParticleTypes.PORTAL, this.tom.posX + x, this.tom.posY + y, this.tom.posZ + z, 1, 0.0d, 0.2d, 0.0d, 0);
					}
				}
			}
			
			((WorldServer)this.tom.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.tom.posX, this.tom.posY + 1.2, this.tom.posZ, 1, 0.0d, 0.2d, 0.0d, 0);			
			this.tom.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
		}
		
		this.tom.noClip = true;
		this.tom.setNoGravity(true);
		this.tom.addVelocity(0.0f, 0.005f, 0.0f);
		((WorldServer)this.tom.world).spawnParticle(EnumParticleTypes.PORTAL, this.tom.posX, this.tom.posY, this.tom.posZ, 4, 0.0d, 0.2d, 0.0d, 0);
		this.runTime++;
	}
	
}

