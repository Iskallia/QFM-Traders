package kaptainwutax.traders.world.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import kaptainwutax.traders.Traders;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDataTomMap extends WorldSavedData {

	private static final String DATA_NAME = Traders.MOD_ID + "_TomMap";
	public static WorldDataTomMap get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldDataTomMap instance = (WorldDataTomMap)storage.getOrLoadData(WorldDataTomMap.class, DATA_NAME);
		
		if(instance == null) {
			instance = new WorldDataTomMap();
			instance.world = world;
			storage.setData(DATA_NAME, instance);			
		}
		
		return instance;
	}
	private HashMap<String, String> tomMap = new HashMap<>();
	private HashMap<String, Long> spawnTimeMap = new HashMap<>();
	
	private World world;
	
	public WorldDataTomMap() {
		super(DATA_NAME);
	}

	public WorldDataTomMap(String name) {
		super(name);
	}

	public Map<String, String> getPlayerMap() {		
		return this.tomMap.entrySet().stream().filter(e -> e.getValue() != null)
			       .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	}
	
	public Map<String, Long> getTimeMap() {
		return this.spawnTimeMap;
	}
	
	public Map<String, String> getTomMap() {
		return this.tomMap;
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {		
		for(int i = 0; nbt.hasKey("key" + i); i++) {
			String key = nbt.getString("key" + i);
			String value = nbt.getString("value" + i);
			this.tomMap.put(key, value.equals("null") ? null : value);
		}
		
		for(int i = 0; nbt.hasKey("time_key" + i); i++) {
			String key = nbt.getString("time_key" + i);
			long value = nbt.hasKey("time_value" + i) ? nbt.getLong("time_value" + i) : -1;
			this.spawnTimeMap.put(key, value);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		int i = 0;
		
		for(Map.Entry<String, String> e: this.tomMap.entrySet()) {
			nbt.setString("key" + i, e.getKey());
			nbt.setString("value" + i, e.getValue() == null ? "null" : e.getValue());
			i++;
		}
		
		i = 0;
		
		for(Entry<String, Long> e: this.spawnTimeMap.entrySet()) {
			nbt.setString("time_key" + i, e.getKey());
			nbt.setLong("time_value" + i, e.getValue() == null ? -1 : e.getValue());
			i++;
		}
		
		return nbt;
	}	

	
}
