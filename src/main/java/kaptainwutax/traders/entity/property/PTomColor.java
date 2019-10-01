package kaptainwutax.traders.entity.property;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.util.Product;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class PTomColor extends PTom {
	
	public int colorId;
	private List<IBlockState> possibleBlockStates = new ArrayList<>();

	public int coloredBlocksCount;
	private int id;
	
	public PTomColor(int id) {
		this.id = id;
		this.setColorId(RAND.nextInt(InitConfig.CONFIG_TOM_NEEDS.FAVOURITE_COLORS.size()));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {	
		String key = "PTomColor" + id;
		if(!compound.hasKey(key))return;
		NBTTagCompound nbt = compound.getCompoundTag(key);
		
		if(nbt.hasKey("ColorId"))this.setColorId(nbt.getInteger("ColorId"));
		if(nbt.hasKey("ColoredBlocksCount"))this.coloredBlocksCount = nbt.getInteger("ColoredBlocksCount");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setInteger("ColorId", this.colorId);
		nbt.setInteger("ColoredBlocksCount", this.coloredBlocksCount);
		
		compound.setTag("PTomColor" + this.id, nbt);
	}
	
	private void setColorId(int colorId) {
		this.colorId = colorId;
		this.possibleBlockStates.clear();
		
		List<Product> possibleProducts = InitConfig.CONFIG_TOM_NEEDS.FAVOURITE_COLORS.get(this.colorId);
		
		for(Product product: possibleProducts) {
			Block block = Block.getBlockFromItem(product.getItem());
			this.possibleBlockStates.add(block.getStateFromMeta(product.getMetadata()));
		}	
	}
	
	private boolean matchesBlock(IBlockState blockState) {
		for(int i = 0; i < this.possibleBlockStates.size(); i++) {
			IBlockState possibleBlockState = this.possibleBlockStates.get(i);
			Block possibleBlock = possibleBlockState.getBlock();
			Block block = blockState.getBlock();
			
			if(possibleBlock != block)continue;
			if(possibleBlock.getMetaFromState(possibleBlockState) != block.getMetaFromState(blockState))continue;
			return true;
		}		
		
		return false;
	}

	@Override
	public void tick(EntityTom tom, boolean isClient) {
		if(!isClient && (tom.ticksExisted + 10) % 30 == 0) {
			this.coloredBlocksCount = 0;
			BlockPos center = tom.getPosition();
			
			for(int x = center.getX() - 4; x <= center.getX() + 4; x++) {
				for(int z = center.getZ() - 4; z <= center.getZ() + 4; z++) {
					for(int y = center.getY() - 2; y <= center.getY() + 5; y++) {
						BlockPos pos = new BlockPos(x, y, z);
						IBlockState state = tom.world.getBlockState(pos);
						if(this.matchesBlock(state)) {
							this.coloredBlocksCount++;
						}
					}
				}
			}
		}
	}
	
	public int getColoredBlocksCount() {
		return this.coloredBlocksCount;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.coloredBlocksCount);
		buf.writeInt(this.colorId);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.coloredBlocksCount = buf.readInt();
		this.colorId = buf.readInt();
	}
	
}
