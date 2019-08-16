package kaptainwutax.traders.entity.ai;

import java.util.Random;

import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.init.InitSoundEvent;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.SoundCategory;

public class EntityTraderAILeave extends EntityAIBase {

	private EntityTrader trader;
	private int runTime;
	
	public EntityTraderAILeave(EntityTrader trader) {
		this.trader = trader;
	}

	@Override
	public boolean shouldExecute() {
		return true;
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
			this.trader.world.playSound(null, this.trader.getPosition(), InitSoundEvent.ISKALL_LAUGH, SoundCategory.PLAYERS, 15.0f, 1.0f - new Random().nextFloat() * 0.1f);
		}
		
		if(this.runTime == 60) {		
			trader.setDead();
		}
		
		this.trader.noClip = true;
		this.trader.setNoGravity(true);
		this.trader.addVelocity((new Random().nextFloat() - 0.5f), 0.01f, (new Random().nextFloat() - 0.5f));
		this.runTime++;
	}
	
}
