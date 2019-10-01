package kaptainwutax.traders.block;

import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.init.InitSoundEvent;
import kaptainwutax.traders.world.data.WorldDataTime;
import kaptainwutax.traders.world.data.WorldDataTomMap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockTradingTable extends BlockPOI {

	public BlockTradingTable(String name) {
		super(Material.WOOD, name);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		
		if(world.isRemote || !(placer instanceof EntityPlayer))return;	

		EntityPlayer player = (EntityPlayer)placer;
		String playerUUID = player.getUniqueID().toString();
		
		WorldDataTomMap tomData = WorldDataTomMap.get(world);
		WorldDataTime timeData = WorldDataTime.get(world);

		if(tomData.getTomMap().get(playerUUID) != null)return;
				
		long time = timeData.getTime().getTime();
		long last = tomData.getTimeMap().get(playerUUID) == null ? -1 : tomData.getTimeMap().get(playerUUID);
		long diff = time - last;

		if(last >= 0 && diff >= 0 && diff < 24000 * 7)return;
		
		for(int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
			for(int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
				BlockPos spawnPos = new BlockPos(x, pos.getY(), z);
				if(spawnPos.equals(pos))continue;
				
				EntityTrader trader = new EntityTom(world);
				trader.setPosition(spawnPos.getX() + 0.5f, spawnPos.getY(), spawnPos.getZ() + 0.5f);
				
				if(trader.getCanSpawnHere()) {		
					world.spawnEntity(trader);
					this.postSpawn(trader);
					tomData.getTomMap().put(playerUUID, trader.getUniqueID().toString());
					return;
				} 
			}
		}
	}

	private void postSpawn(EntityTrader trader) {
		for(double x = -0.5d; x < 0.8d - 0.5d; x += 0.1d) {
			for(double z = -0.5d; z < 0.8d - 0.5d; z += 0.1d) {
				for(double y = 0.0d; y < 1.8d; y += 0.1d) {
					((WorldServer)trader.world).spawnParticle(EnumParticleTypes.PORTAL, trader.posX + x, trader.posY + y, trader.posZ + z, 1, 0.0d, 0.2d, 0.0d, 0);
				}
			}
		}
		
		trader.world.playSound(null, trader.getPosition(), InitSoundEvent.VILLAGER_VANISH, SoundCategory.PLAYERS, 4.0f, 3.2f);	
	}
	
}
