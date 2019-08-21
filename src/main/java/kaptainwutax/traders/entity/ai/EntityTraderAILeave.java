package kaptainwutax.traders.entity.ai;

import java.util.Random;

import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.init.InitSoundEvent;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;

public class EntityTraderAILeave extends EntityAIBase {

	private EntityTrader trader;
	private int runTime;
	
	public EntityTraderAILeave(EntityTrader trader) {
		this.trader = trader;
	}

	@Override
	public boolean shouldExecute() {
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return true;
	}
	
	@Override
	public void startExecuting() {
		this.runTime = 0;
	}
	
	@Override
	public void updateTask() {		
		super.updateTask();

		if(this.runTime == 40) {
			this.trader.world.playSound(null, this.trader.getPosition(), InitSoundEvent.VILLAGER_VANISH, SoundCategory.PLAYERS, 5.0f, 1.0f - new Random().nextFloat() * 0.1f);
		}
		
		if(this.runTime == 60) {					
			for(double x = 0; x < 0.8; x += 0.1) {
				for(double z = 0; z < 0.8; z += 0.1) {
					for(double y = 0; y < 1.8; y += 0.1) {
						((WorldServer)this.trader.world).spawnParticle(EnumParticleTypes.PORTAL, this.trader.posX + x, this.trader.posY + y, this.trader.posZ + z, 1, 0.0d, 0.2d, 0.0d, 0);
					}
				}
			}
			
			((WorldServer)this.trader.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.trader.posX, this.trader.posY + 1.2, this.trader.posZ, 1, 0.0d, 0.2d, 0.0d, 0);
			trader.setDead();
		}
		
		this.trader.noClip = true;
		this.trader.setNoGravity(true);
		this.trader.addVelocity((new Random().nextFloat() - 0.5f), 0.01f, (new Random().nextFloat() - 0.5f));
		((WorldServer)this.trader.world).spawnParticle(EnumParticleTypes.PORTAL, this.trader.posX, this.trader.posY, this.trader.posZ, 4, 0.0d, 0.2d, 0.0d, 0);
		this.runTime++;
	}
	
}
