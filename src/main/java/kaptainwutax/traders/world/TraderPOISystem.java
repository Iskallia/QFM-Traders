package kaptainwutax.traders.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import kaptainwutax.traders.world.storage.ChunkStoragePOI;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class TraderPOISystem {

	protected HashMap<Long, ChunkStoragePOI> HASH_TO_CHUNK = new HashMap<>();
	
	public List<BlockPos> getAllInRange(BlockPos pos, int radius) {
		List<BlockPos> nearbyPOIs = new ArrayList<>();
		
		int lowX = pos.getX() - radius;
		int highX = pos.getX() + radius;
		int lowZ = pos.getZ() - radius;
		int highZ = pos.getZ() + radius;
		
		for(int x = lowX; x <= highX; x += 16) {
			for(int z = lowZ; z <= highZ; z += 16) {
				BlockPos chunkPos = new BlockPos(x >> 4, 0, z >> 4);
				ChunkStoragePOI storage = getOrCreateStorage(chunkPos.toLong());
				Set<BlockPos> allPOIs = storage.getAll();
				
				for(BlockPos poi: allPOIs) {
					if(pos.distanceSq(poi) <= radius * radius) {
						nearbyPOIs.add(poi);
					}
				}
			}
		}
		
		return nearbyPOIs;
	}
	
	public ChunkStoragePOI getOrCreateStorage(long hash) {
		if(HASH_TO_CHUNK.containsKey(hash)) {
			return HASH_TO_CHUNK.get(hash);
		} 
		
		ChunkStoragePOI storage = new ChunkStoragePOI();
		storage.setHash(hash);
		HASH_TO_CHUNK.put(hash, storage);
		
		return storage;
	}
	
	public void readFromNBT(NBTTagList list) {				
		list.forEach(compound -> {
			ChunkStoragePOI chunkStorage = new ChunkStoragePOI();
			chunkStorage.readFromNBT(compound);
			HASH_TO_CHUNK.put(chunkStorage.getHash(), chunkStorage);
		});
	}
	
	public void registerPOI(BlockPos pos) {
		BlockPos chunkPos = new BlockPos(pos.getX() >> 4, 0, pos.getZ() >> 4);
		ChunkStoragePOI storage = getOrCreateStorage(chunkPos.toLong());
		storage.registerPOI(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
	}

	public void releasePOI(BlockPos pos) {
		BlockPos chunkPos = new BlockPos(pos.getX() >> 4, 0, pos.getZ() >> 4);
		ChunkStoragePOI storage = getOrCreateStorage(chunkPos.toLong());
		storage.releasePOI(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
	}

	public NBTBase writeToNBT() {
		NBTTagList storage = new NBTTagList();
		
		for(ChunkStoragePOI chunkStorage: HASH_TO_CHUNK.values()) {	
			if(!chunkStorage.isEmpty())storage.appendTag(chunkStorage.writeToNBT());
		}
			
		return storage;
	}
	
}
