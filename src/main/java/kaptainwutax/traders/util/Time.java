package kaptainwutax.traders.util;

public class Time {
	
	public static int WEEK;
	public static int DAY_OF_WEEK;
	public static int HOUR_OF_DAY;
	public static int MINUTE_OF_HOUR;
	
	public static void updateTime(long worldTime) {			
		WEEK = (int)(worldTime / 168000);
		DAY_OF_WEEK = WEEK / 24000;	
		HOUR_OF_DAY = (int)(worldTime % 24000) / 1000;
		MINUTE_OF_HOUR = (int) ((worldTime % 1000) / 16.6666f);
	}
	
}
