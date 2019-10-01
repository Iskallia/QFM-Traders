package kaptainwutax.traders.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.block.BlockTradingTable;
import kaptainwutax.traders.entity.ai.EntityTraderAIDie;
import kaptainwutax.traders.entity.property.PTom;
import kaptainwutax.traders.entity.property.PTomBlocks;
import kaptainwutax.traders.entity.property.PTomColor;
import kaptainwutax.traders.entity.property.PTomPet;
import kaptainwutax.traders.entity.property.Property;
import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitPacket;
import kaptainwutax.traders.init.InitTrade;
import kaptainwutax.traders.net.packet.PacketS2CTomHappiness;
import kaptainwutax.traders.net.packet.PacketS2CTomPOI;
import kaptainwutax.traders.net.packet.PacketS2CTomProperties;
import kaptainwutax.traders.util.Players;
import kaptainwutax.traders.util.ServerChat;
import kaptainwutax.traders.util.Trade;
import kaptainwutax.traders.world.TraderPOISystem;
import kaptainwutax.traders.world.data.WorldDataPOI;
import kaptainwutax.traders.world.data.WorldDataTime;
import kaptainwutax.traders.world.data.WorldDataTomMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTom extends EntityTrader {
	
	public long joblessStart = -1;
	private BlockPos tradingTable = null;
	
	private int happiness = 80;
	private int maxHappiness = 100;
	private boolean happinessDirty = true;

	private float happinessAnim = 0.0f;

	private FoodStats foodStats = new FoodStats();

	private List<PTom> properties = new ArrayList<>();
	
	public PTomBlocks favouriteBlocks = new PTomBlocks();
	public PTomPet favouritePet = new PTomPet();
	public PTomColor favouriteColor = new PTomColor(1);
	public PTomColor favouriteColor2 = new PTomColor(2);
	
	public EntityTom(World world) {
		super(world, "Tom");
		properties.add(this.favouriteBlocks);
		properties.add(this.favouritePet);
		properties.add(this.favouriteColor);
		properties.add(this.favouriteColor2);
	}
	
    public FoodStats getFoodStats() {
        return this.foodStats;
    }
	
	public void addHappinessServer(int increment) {
		if(this.world.isRemote)return;
		
		this.happiness += increment;
		
		if(this.happiness >= this.getMaxHappiness()) {
			this.happiness = this.getMaxHappiness();
		}
		
		if(this.happiness < 0)this.happiness = 0;
	}
	
	public void addHappinessClient(int increment) {
		if(!this.world.isRemote)return;
		
		this.happiness += increment;
		int sign = (int)Math.signum(increment);
		
		if(sign == 1) {
			this.handleStatusUpdate((byte)14);
		} else if(sign == -1) {
			this.handleStatusUpdate((byte)13);
		}
		
		if(this.happiness >= this.getMaxHappiness()) {
			if(sign == 1)this.handleStatusUpdate((byte)12);
			this.happiness = this.getMaxHappiness();
		}
		
		if(this.happiness < 0)this.happiness = 0;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		this.addHappinessServer(-10);
		return super.attackEntityFrom(source, amount);
	}
	
	public int getHappiness() {
		return this.happiness;
	}
	
	@SideOnly(Side.CLIENT)
	public float getHappinessAnim() {
		this.happinessAnim += (this.happiness - this.happinessAnim) * 0.05f;
		return this.happinessAnim;
	}
	
	@Override
	public ResourceLocation[] getLayers() {
		if(!this.hasPOI()) {			
			return new ResourceLocation[] {
					Traders.getResource("textures/entity/trader/tom_1.png")
			};
		} 

		return new ResourceLocation[] {
				Traders.getResource("textures/entity/trader/tom_1.png"),
				Traders.getResource("textures/entity/trader/tom_2_grayscaled.png"),
				Traders.getResource("textures/entity/trader/tom_3_grayscaled.png")
		};
	}

	public int getMaxHappiness() {
		return this.maxHappiness;
	}
	
	@Override
	public List<Trade> getNewTrades() {
		List<Trade> randomTrades = new ArrayList<Trade>(InitTrade.TOM);
		Collections.shuffle(randomTrades);	
		
		randomTrades = randomTrades.stream()
				.limit(InitConfig.CONFIG_TOM.TRADES_COUNT)
				.collect(Collectors.toList());
		
		return randomTrades;
	}

	public EntityPlayer getParentPlayer() {
		WorldDataTomMap dataTom = WorldDataTomMap.get(this.world);
		
		String playerUUID = dataTom.getPlayerMap().get(this.getUniqueID().toString());
		
		if(playerUUID != null) {
			return Players.getPlayerFromUUID(this.world, playerUUID);
		}
		
		return null;
	}
	
	public boolean hasPOI() {
		return this.tradingTable != null;
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityTraderAIDie(this));
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		
		if(!this.world.isRemote) {			
			WorldDataTomMap dataTom = WorldDataTomMap.get(this.world);
			WorldDataTime dataTime = WorldDataTime.get(this.world);
			
			String playerUUID = dataTom.getPlayerMap().get(this.getUniqueID().toString());
			
			if(playerUUID != null) {
				dataTom.getTimeMap().put(playerUUID, dataTime.getTime().getTime());
				EntityPlayer player = Players.getPlayerFromUUID(this.world, playerUUID);
			}
			
			Map<String, String> map = dataTom.getTomMap();
			
			if(map.containsValue(this.getUniqueID().toString())) {
				for(String key: map.keySet())map.replace(key, this.getUniqueID().toString(), null);
			}
			
			if(this.hasPOI()) {
				this.releasePOI(true);
			}			
		}
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		if(this.dead)return;
		
		if(!this.world.isRemote && !this.hasPOI() && this.ticksExisted % 20 == 0) {
			WorldDataPOI poiData = WorldDataPOI.get(world);
			TraderPOISystem poiSystem = poiData.getPOISystem();
			
			List<BlockPos> nearbyPOIs = poiSystem.getAllInRange(this.getPosition(), 48);
			Collections.shuffle(nearbyPOIs);
			nearbyPOIs = nearbyPOIs.stream().limit(12).collect(Collectors.toList());
			
			for(BlockPos nearbyPOI: nearbyPOIs) {
				if(nearbyPOI.distanceSq(this.getPosition()) > 3.0d) {
					PathNavigate navigator = this.navigator;
					Path path = navigator.getPathToPos(nearbyPOI);
					if(path == null)continue;
					navigator.setPath(path, 0.8f);
				}
				
				this.registerPOI(nearbyPOI);
				break;
			}
		}
		
		if(!this.world.isRemote && this.hasPOI() && this.ticksExisted % 5 == 0) {
			if(this.getPosition().distanceSq(this.tradingTable) > 48.0d * 48.0d) {
				this.releasePOI(true);
			} else {			
				Block block = this.world.getBlockState(this.tradingTable).getBlock();
				
				if(!(block instanceof BlockTradingTable)) {
					this.releasePOI(false);
				}
			}
		}
		
		if(!this.world.isRemote && this.hasPOI() && this.ticksExisted % 20 == 0 && this.tradingTable.distanceSq(this.getPosition()) > 100.0d) {
			PathNavigate navigator = this.navigator;
			Path path = navigator.getPathToPos(this.tradingTable);
			navigator.setPath(path, 0.8f);
		}
		
		if(!this.world.isRemote && this.ticksExisted % 5 == 0) {
			InitPacket.PIPELINE.sendToAllTracking(new PacketS2CTomPOI(this.tradingTable, this), this);
			InitPacket.PIPELINE.sendToAllTracking(new PacketS2CTomHappiness(this.getHappiness(), this.foodStats.getFoodLevel(), this.isPotionActive(MobEffects.HUNGER), this.getHealth(), this), this);
			InitPacket.PIPELINE.sendToAllTracking(new PacketS2CTomProperties(this), this);
		}
		
		boolean hasParentPlayer = this.getParentPlayer() != null;
		
		if(!this.world.isRemote && hasParentPlayer && this.ticksExisted % 10000 == 0) {
			int food = this.foodStats.getFoodLevel() - 1;
			this.foodStats.setFoodLevel(MathHelper.clamp(food, 0, 20));
		}
		
		if(!this.world.isRemote && this.isPotionActive(MobEffects.HUNGER) && this.ticksExisted % 20 == 0) {
			int food = this.foodStats.getFoodLevel() - 1;
			this.foodStats.setFoodLevel(MathHelper.clamp(food, 0, 20));
		}
		
		if(!this.world.isRemote && this.foodStats.getFoodLevel() == 0 && this.ticksExisted % 1200 == 0) {
			this.attackEntityFrom(DamageSource.STARVE, 1.0f);
		}
		
		for(PTom p: this.properties) {
			p.tick(this, this.world.isRemote);
		}
		
		if(!this.world.isRemote) {
			this.happiness = 0;
			
			int petValue = this.favouritePet.getEntityCount();
			petValue = MathHelper.clamp(petValue, 0, 5);		
			this.happiness += petValue * 3;

			int colorValue = this.favouriteColor.getColoredBlocksCount() + this.favouriteColor2.getColoredBlocksCount();
			colorValue /= 6;		
			this.happiness += colorValue;
			
			int blockValue = this.favouriteBlocks.getBlocksCount();
			blockValue /= 12;
			this.happiness += blockValue;
			
			this.happiness *= (this.getHealth() / this.getMaxHealth());
			
			if(this.isPotionActive(MobEffects.HUNGER) || this.foodStats.getFoodLevel() <= 4) {
				this.happiness -= 10;
			}
			
			if(this.happiness > 100)this.happiness = 100;
			if(this.happiness < 0)this.happiness = 0;
		}

	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		
		if(!this.world.isRemote) {		
			if(stack.getItem() instanceof ItemFood) {
	            ItemFood food = (ItemFood)stack.getItem();           
	            if(!player.isCreative())stack.shrink(1);           
	            this.foodStats.addStats(food, stack);
	            food.onItemUseFinish(stack, this.world, this);
	            stack.shrink(-1);

	            if(food == Items.ROTTEN_FLESH)this.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200));
	            else if(food == Items.POISONOUS_POTATO)this.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 50));
	            
	            this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.NEUTRAL, 1.0f, this.rand.nextFloat() * 0.4f + 0.6f);
	            return true;
			}	
		}
		
		if(this.hasPOI())return super.processInteract(player, hand);
		else return true;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);		
		if(compound.hasKey("POIHash"))this.registerPOI(BlockPos.fromLong(compound.getLong("POIHash")));
		if(compound.hasKey("JoblessStart"))this.joblessStart = compound.getLong("JoblessStart");
		if(compound.hasKey("Happiness"))this.happiness = compound.getInteger("Happiness");

		for(Property p: this.properties) {
			p.readFromNBT(compound);
		}
	}
	
	public void registerPOI(BlockPos pos) {
		this.tradingTable = pos;
		WorldDataPOI poiData = WorldDataPOI.get(world);
		TraderPOISystem poiSystem = poiData.getPOISystem();
		poiSystem.releasePOI(this.tradingTable);

		this.joblessStart = -1;
		this.world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 2.0f, this.rand.nextFloat() * 0.4f + 0.6f);
	}
	
	public void releasePOI(boolean exists) {
		WorldDataPOI poiData = WorldDataPOI.get(world);
		TraderPOISystem poiSystem = poiData.getPOISystem();
		if(exists)poiSystem.registerPOI(this.tradingTable);
		this.tradingTable = null;
	
		WorldDataTime dataTime = WorldDataTime.get(world);
		this.joblessStart = dataTime.getTime().getTime();
		
		EntityPlayer player = this.getParentPlayer();
		
		if(player != null) {
			ServerChat.whisper((EntityPlayerMP)player, this, InitConfig.CONFIG_TOM.POI_LOSS_PHRASES);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void setHappiness(int value) {
		this.addHappinessClient(value - this.happiness);
	}
	
	@SideOnly(Side.CLIENT)
	public void setTradingTable(BlockPos tradingTable) {
		this.tradingTable = tradingTable;		
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if(this.tradingTable != null)compound.setLong("POIHash", this.tradingTable.toLong());
		compound.setLong("JoblessStart", this.joblessStart);
		compound.setInteger("Happiness", this.happiness);
		
		for(Property p: this.properties) {
			p.writeToNBT(compound);
		}
	}
	
	@Override
	public void handleStatusUpdate(byte id) {
		if(this.world.isRemote)super.handleStatusUpdate(id);		
	}
	
}