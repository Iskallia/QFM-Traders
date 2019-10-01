package kaptainwutax.traders.entity.property;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.util.Product;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class PTomBlocks extends PTom {
	
	public List<Product> products;
	public List<IBlockState> blocks;
	public List<Integer> counts;
	
	public int blocksCount;

	public PTomBlocks() {		
		List<Product> randomProducts = InitConfig.CONFIG_TOM_NEEDS.FAVOURITE_BLOCKS;		
		Collections.shuffle(randomProducts);	
		
		this.products = randomProducts.stream().limit(5).collect(Collectors.toList());
		this.blocks = this.products.stream()
				.map(product -> Block.getBlockFromItem(product.getItem()).getStateFromMeta(product.getMetadata()))
				.collect(Collectors.toList());
		this.counts = new ArrayList<>();
		this.blocks.forEach(dummy -> this.counts.add(0));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(!compound.hasKey("PTomBlocks"))return;
		NBTTagCompound nbt = compound.getCompoundTag("PTomBlocks");
		
		if(nbt.hasKey("BlocksCount"))this.blocksCount = nbt.getInteger("BlocksCount");
		
		if(nbt.hasKey("products")) {
			NBTTagList productsList = nbt.getTagList("products", 10);
			this.products.clear();
			this.blocks.clear();
			this.counts.clear();
			
			productsList.forEach(raw -> {
				if(raw instanceof NBTTagCompound) {
					NBTTagCompound nbt1 = (NBTTagCompound)raw;
					Product product = new Product(Item.getByNameOrId(nbt1.getString("name")), nbt1.getInteger("meta"), 0, null);
					this.products.add(product);
					this.blocks.add(Block.getBlockFromItem(product.getItem()).getStateFromMeta(product.getMetadata()));
				}
			});
		}
		
		if(nbt.hasKey("counts"))this.counts = Arrays.stream(nbt.getIntArray("counts")).boxed().collect(Collectors.toList());
		else this.blocks.forEach(dummy -> this.counts.add(0));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setInteger("BlocksCount", this.blocksCount);
		
		NBTTagList productsList = new NBTTagList();
		
		for(Product product: this.products) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setString("name", product.getName());
			nbt1.setInteger("meta", product.getMetadata());
			productsList.appendTag(nbt1);
		}
		
		nbt.setTag("products", productsList);
		nbt.setIntArray("counts", this.counts.stream().mapToInt(i -> i).toArray());
		compound.setTag("PTomBlocks", nbt);
	}
	
	private int matchesBlock(IBlockState blockState) {
		for(int i = 0; i < this.blocks.size(); i++) {
			IBlockState possibleBlockState = this.blocks.get(i);
			Block possibleBlock = possibleBlockState.getBlock();
			Block block = blockState.getBlock();
						
			if(possibleBlock != block)continue;
			return i;
		}		
		
		return -1;
	}

	@Override
	public void tick(EntityTom tom, boolean isClient) {
		if(!isClient && (tom.ticksExisted + 20) % 30 == 0) {
			this.blocksCount = 0;
			BlockPos center = tom.getPosition();
			this.counts.clear();
			this.blocks.forEach(dummy -> this.counts.add(0));
			
			for(int x = center.getX() - 4; x <= center.getX() + 4; x++) {
				for(int z = center.getZ() - 4; z <= center.getZ() + 4; z++) {
					for(int y = center.getY() - 2; y <= center.getY() + 5; y++) {
						BlockPos pos = new BlockPos(x, y, z);
						IBlockState state = tom.world.getBlockState(pos);
						
						int match = this.matchesBlock(state);
						
						if(match >= 0) {
							this.counts.set(match, this.counts.get(match) + 1);
							this.blocksCount++;
						}
					}
				}
			}
		}
	}
	
	public int getBlocksCount() {
		return this.blocksCount;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.blocksCount);
		
		int productsCount = this.products.size();
		buf.writeInt(productsCount);
		
		for(int i = 0; i < productsCount; i++) {
			String name = this.products.get(i).getName();
			int meta = this.products.get(i).getMetadata();
			buf.writeInt(name.length());
			buf.writeCharSequence(name, Charset.defaultCharset());
			buf.writeInt(meta);
			buf.writeInt(this.counts.get(i));
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.blocksCount = buf.readInt();
		
		int productsCount = buf.readInt();
		this.products.clear();
		this.blocks.clear();
		this.counts.clear();
		
		for(int i = 0; i < productsCount; i++) {
			String name = buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
			int meta = buf.readInt();
			
			Product product = new Product(Item.getByNameOrId(name), meta, 0, null);
			this.products.add(product);
			this.blocks.add(Block.getBlockFromItem(product.getItem()).getStateFromMeta(product.getMetadata()));
			this.counts.add(buf.readInt());
		}
	}
	
}
