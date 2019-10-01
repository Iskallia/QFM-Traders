package kaptainwutax.traders.world.data;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.world.TraderPOISystem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDataPOI extends WorldSavedData {
	
	private static final String DATA_NAME = Traders.MOD_ID + "_POI";
	public static WorldDataPOI get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldDataPOI instance = (WorldDataPOI)storage.getOrLoadData(WorldDataPOI.class, DATA_NAME);
		
		if(instance == null) {
			instance = new WorldDataPOI();
			storage.setData(DATA_NAME, instance);
		}
		  
		return instance;
	}
	
	private TraderPOISystem poiSystem = new TraderPOISystem();
	
	public WorldDataPOI() {
		super(DATA_NAME);
	}

	public WorldDataPOI(String name) {
		super(name);
	}

	public TraderPOISystem getPOISystem() {
		return this.poiSystem;
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("data")) {
			this.poiSystem.readFromNBT(nbt.getTagList("data", 10));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("data", this.poiSystem.writeToNBT());
		return nbt;
	}	

}
