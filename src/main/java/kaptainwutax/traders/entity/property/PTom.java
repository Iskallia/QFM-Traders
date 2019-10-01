package kaptainwutax.traders.entity.property;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.entity.EntityTom;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PTom extends Property {

	@Override
	public void tick(boolean isClient) {		
	}
	
	public abstract void tick(EntityTom tom, boolean isClient);
	
	public abstract void toBytes(ByteBuf buf);
	public abstract void fromBytes(ByteBuf buf);

}
