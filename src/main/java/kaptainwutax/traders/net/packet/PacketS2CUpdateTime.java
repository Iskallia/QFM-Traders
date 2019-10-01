package kaptainwutax.traders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.util.Time;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketS2CUpdateTime implements IMessage {

	public static class PacketS2CUpdateTimeHandler implements IMessageHandler<PacketS2CUpdateTime, IMessage> {

		@Override
		public IMessage onMessage(PacketS2CUpdateTime message, MessageContext ctx) {
			Time time = Time.getClient();
			time.setTime(message.globalTime);	
			return null;
		}

	}
	
	private long globalTime;
	
	public PacketS2CUpdateTime() {

	}
	
	public PacketS2CUpdateTime(long globalTime) {
		this.globalTime = globalTime;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.globalTime = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.globalTime);
	}

}
