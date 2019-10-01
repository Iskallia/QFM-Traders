package kaptainwutax.traders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketS2CTomPOI implements IMessage {

	private long posHash;
	
	private int entityId;
	
	public PacketS2CTomPOI() {

	}
	
	public PacketS2CTomPOI(BlockPos pos, EntityTrader trader) {
		this.posHash = pos != null ? pos.toLong() : -1;
		this.entityId = trader.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.posHash = buf.readLong();
		this.entityId = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.posHash);
		buf.writeInt(this.entityId);
	}
	
	public static class PacketS2CTomPOIHandler implements IMessageHandler<PacketS2CTomPOI, IMessage> {

		@Override
		public IMessage onMessage(PacketS2CTomPOI message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP player = minecraft.player;
			World world = player.world;
			
			Entity entity = world.getEntityByID(message.entityId);
			if(entity == null || entity.isDead || !(entity instanceof EntityTom))return null;
			
			EntityTom tom = (EntityTom)entity;
			tom.setTradingTable(message.posHash == -1 ? null : BlockPos.fromLong(message.posHash));
			return null;
		}

	}

}
