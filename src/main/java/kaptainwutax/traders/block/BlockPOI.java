package kaptainwutax.traders.block;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.world.TraderPOISystem;
import kaptainwutax.traders.world.data.WorldDataPOI;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPOI extends Block {

	public BlockPOI(Material material, String name) {
		super(material);
		this.setRegistryName(Traders.getResource(name));
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		if(world.isRemote)return;
		WorldDataPOI poiData = WorldDataPOI.get(world);
		TraderPOISystem poiSystem = poiData.getPOISystem();
		poiSystem.registerPOI(pos);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(world, pos, state, player);
		if(world.isRemote)return;
		WorldDataPOI poiData = WorldDataPOI.get(world);
		TraderPOISystem poiSystem = poiData.getPOISystem();
		poiSystem.releasePOI(pos);
	}

}
