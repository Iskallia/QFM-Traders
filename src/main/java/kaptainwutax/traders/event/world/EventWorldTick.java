	package kaptainwutax.traders.event.world;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.init.InitPacket;
import kaptainwutax.traders.init.InitSpawner;
import kaptainwutax.traders.net.packet.PacketS2CUpdateTime;
import kaptainwutax.traders.util.Time;
import kaptainwutax.traders.world.data.WorldDataTime;
import kaptainwutax.traders.world.spawner.Spawner;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Traders.MOD_ID)
public class EventWorldTick {

	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if(event.side != Side.SERVER)return;
		
		World world = event.world;
		WorldDataTime data = WorldDataTime.get(world);
		Time time = data.getTime();
		
		long worldTime = world.getWorldTime() % 24000;
		long increment = 0;
		
		if(time.LAST_TIME_OF_DAY == -1) {
			time.LAST_TIME_OF_DAY = worldTime;
		} else {
			while(time.LAST_TIME_OF_DAY != worldTime) {
				increment++;
				time.LAST_TIME_OF_DAY++;
				time.LAST_TIME_OF_DAY %= 24000;
			}
			
			time.setTime(time.getTime() + increment);
		}

		if(world.getTotalWorldTime() % 10 == 0) {
			InitPacket.PIPELINE.sendToDimension(new PacketS2CUpdateTime(time.getTime()), world.provider.getDimension());
		}
		
		if(world.getTotalWorldTime() % 100 == 0) {
			//FakePlayer player = new FakePlayer((WorldServer)world, new GameProfile(null, "KaptainWutax"));
			//player.setPosition(0, 100, 0);
			FakePlayerFactory.getMinecraft((WorldServer)world);
			System.out.println("spawned");
		}
		
		for(Spawner spawner: InitSpawner.SPAWNERS) {
			spawner.tick(world);
		}
	}
	
}
