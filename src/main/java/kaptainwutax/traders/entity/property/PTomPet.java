package kaptainwutax.traders.entity.property;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.init.InitConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class PTomPet extends PTom {
	
	public String entityId;
	private Class<? extends Entity> entity;
	
	private int entityCount;
	
	public PTomPet() {		
		List<String> pets = InitConfig.CONFIG_TOM_NEEDS.FAVOURITE_PETS;
		if(pets.size() > 0)this.setEntityId(pets.get(RAND.nextInt(pets.size())));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(!compound.hasKey("PTomPet"))return;
		NBTTagCompound nbt = compound.getCompoundTag("PTomPet");
		
		if(nbt.hasKey("EntityId"))this.setEntityId(nbt.getString("EntityId"));
		if(nbt.hasKey("EntityCount"))this.entityCount = nbt.getInteger("EntityCount");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setString("EntityId", this.entityId);
		nbt.setInteger("EntityCount", this.entityCount);
		
		compound.setTag("PTomPet", nbt);
	}
	
	private void setEntityId(String entityId) {
		this.entityId = entityId;
		this.entity = EntityList.getClass(new ResourceLocation(this.entityId));
	}

	@Override
	public void tick(EntityTom tom, boolean isClient) {
		if(!isClient && tom.ticksExisted % 30 == 0) {
			this.entityCount = 
				tom.world.getEntitiesWithinAABB(
						this.entity, 
						new AxisAlignedBB(tom.getPosition()).grow(6.0d, 5.0d, 6.0d), 
						null
				).size();
			
			this.entityCount = MathHelper.clamp(this.entityCount, 0, 5);
		}
	}
	
	public int getEntityCount() {
		return this.entityCount;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId.length());
		buf.writeCharSequence(this.entityId, Charset.defaultCharset());
		buf.writeInt(this.entityCount);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.setEntityId(buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString());
		this.entityCount = buf.readInt();
	}
	
}
