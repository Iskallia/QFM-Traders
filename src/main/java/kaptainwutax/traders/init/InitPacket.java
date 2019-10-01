package kaptainwutax.traders.init;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.net.packet.PacketS2CSyncTrades;
import kaptainwutax.traders.net.packet.PacketS2CSyncTrades.PacketS2CSyncTradesHandler;
import kaptainwutax.traders.net.packet.PacketS2CTomHappiness;
import kaptainwutax.traders.net.packet.PacketS2CTomHappiness.PacketS2CTomHappinessHandler;
import kaptainwutax.traders.net.packet.PacketS2CTomPOI;
import kaptainwutax.traders.net.packet.PacketS2CTomPOI.PacketS2CTomPOIHandler;
import kaptainwutax.traders.net.packet.PacketS2CTomProperties;
import kaptainwutax.traders.net.packet.PacketS2CTomProperties.PacketS2CTomPropertiesHandler;
import kaptainwutax.traders.net.packet.PacketS2CUpdateTime;
import kaptainwutax.traders.net.packet.PacketS2CUpdateTime.PacketS2CUpdateTimeHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class InitPacket {

	public static SimpleNetworkWrapper PIPELINE = null;
    private static int packetId = 0;

    private static int nextId() {
        return packetId++;
    }

    public static void registerPackets() {
    	PIPELINE = NetworkRegistry.INSTANCE.newSimpleChannel(Traders.MOD_ID);
    	PIPELINE.registerMessage(PacketS2CSyncTradesHandler.class, PacketS2CSyncTrades.class, nextId(), Side.CLIENT);
    	PIPELINE.registerMessage(PacketS2CUpdateTimeHandler.class, PacketS2CUpdateTime.class, nextId(), Side.CLIENT);
    	PIPELINE.registerMessage(PacketS2CTomPOIHandler.class, PacketS2CTomPOI.class, nextId(), Side.CLIENT);
    	PIPELINE.registerMessage(PacketS2CTomHappinessHandler.class, PacketS2CTomHappiness.class, nextId(), Side.CLIENT);
    	PIPELINE.registerMessage(PacketS2CTomPropertiesHandler.class, PacketS2CTomProperties.class, nextId(), Side.CLIENT);
	}
    
}
