package kaptainwutax.traders.world.storage;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class ChunkStoragePOI {

	private long hash;
	private HashSet<BlockPos> STORAGE = new HashSet<BlockPos>();
	
	public Set<BlockPos> getAll() {
		return this.STORAGE;
	}

	public long getHash() {
		return this.hash;
	}
	
	public boolean hasPOI(BlockPos pos) {
		return STORAGE.contains(pos);
	}

	public boolean isEmpty() {
		return STORAGE.isEmpty();
	}

	public void readFromNBT(NBTBase nbtBase) {
		NBTTagCompound nbt = (NBTTagCompound)nbtBase;
		this.setHash(nbt.getLong("hash"));
		NBTTagList pois = nbt.getTagList("pois", 10);

		pois.forEach(compound -> {				
			this.registerPOI(BlockPos.fromLong(((NBTTagCompound)compound).getLong("hash")));
		});
	}

	public void registerPOI(BlockPos pos) {
		STORAGE.add(pos);		
	}

	public void releasePOI(BlockPos pos) {
		STORAGE.remove(pos);		
	}

	public void setHash(long hash) {
		this.hash = hash;		
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		
		compound.setLong("hash", this.hash);
		
		NBTTagList pois = new NBTTagList();
		
		for(BlockPos pos: STORAGE) {
			NBTTagCompound poi = new NBTTagCompound();
			poi.setLong("hash", pos.toLong());
			pois.appendTag(poi);
		}
		
		compound.setTag("pois", pois);
		return compound;
	}

}
