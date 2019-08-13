package kaptainwutax.traders.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time {

	public static Calendar GAME_DATE = GregorianCalendar.getInstance();
	
	public static void updateTime(long worldTime) {
		GAME_DATE.setTimeInMillis(worldTime);
		GAME_DATE.add(Calendar.YEAR, 2019 - 1970);
		System.out.println(GAME_DATE.getTime());
		
		/*
		int week = (int)(worldTime / 168000);
		GAME_DATE.add(Calendar.WEEK_OF_YEAR, week);
		Calendar.M
		long timeOfWeek = worldTime % 168000;
		
		long day = timeOfWeek / 7;
		long timeOfDay = timeOfWeek % 24000;*/
	}
	
}
