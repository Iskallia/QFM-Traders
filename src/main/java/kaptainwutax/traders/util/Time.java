package kaptainwutax.traders.util;

import net.minecraft.world.World;

public class Time {
	
	private static Time CLIENT = new Time();
	
	private long GLOBAL_TIME = 0;
	private long LAST_GLOBAL_TIME = 0;
	
	public long LAST_TIME_OF_DAY = 0;
	
	public int WEEK;
	public int DAY_OF_WEEK;
	public int HOUR_OF_DAY;
	public int MINUTE_OF_HOUR;
	
	public long getTime() {
		return GLOBAL_TIME;
	}
	
	public void setTime(long newTime) {
		GLOBAL_TIME = newTime;
		updateTime();
	}
	
	private void updateTime() {			
		if(GLOBAL_TIME == LAST_GLOBAL_TIME)return;
		
		WEEK = (int)(GLOBAL_TIME / 168000);
		DAY_OF_WEEK = (int)((GLOBAL_TIME % 168000) / 24000);	
		HOUR_OF_DAY = (int)(GLOBAL_TIME % 24000) / 1000;
		MINUTE_OF_HOUR = (int)((GLOBAL_TIME % 1000) / 16.6666f);
		LAST_GLOBAL_TIME = GLOBAL_TIME;
	}
	
	public void resetTime(World world) {
		LAST_GLOBAL_TIME = 0;
		GLOBAL_TIME = 0;
		world.getWorldInfo().setWorldTime(LAST_TIME_OF_DAY = 18000);
	}
	
	public static Time getClient() {
		return CLIENT;
	}
	
}
