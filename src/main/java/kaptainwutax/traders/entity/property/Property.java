package kaptainwutax.traders.entity.property;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Property {

	public static final Random RAND = new Random();
	
	public abstract void readFromNBT(NBTTagCompound compound);	
	public abstract void writeToNBT(NBTTagCompound compound);
	public abstract void tick(boolean isClient);
	
}
