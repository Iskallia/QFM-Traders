package kaptainwutax.traders.world.data;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.util.Time;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDataTime extends WorldSavedData {
	
	private static final String DATA_NAME = Traders.MOD_ID + "_Time";
	public static WorldDataTime get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldDataTime instance = (WorldDataTime)storage.getOrLoadData(WorldDataTime.class, DATA_NAME);
		
		if(instance == null) {
			instance = new WorldDataTime();
			storage.setData(DATA_NAME, instance);
		}
		  
		return instance;
	}
	
	private Time time = new Time();
	
	public WorldDataTime() {
		super(DATA_NAME);
	}

	public WorldDataTime(String name) {
		super(name);
	}

	public Time getTime() {
		return this.time;
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("time")) {
			this.time.setTime(nbt.getLong("time"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setLong("time", this.time.getTime());
		return compound;
	}	

}
