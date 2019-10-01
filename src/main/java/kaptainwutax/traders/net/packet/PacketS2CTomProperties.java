package kaptainwutax.traders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.EntityTrader;
import kaptainwutax.traders.entity.property.PTomBlocks;
import kaptainwutax.traders.entity.property.PTomColor;
import kaptainwutax.traders.entity.property.PTomPet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketS2CTomProperties implements IMessage {

	private int entityId;

	private PTomBlocks blocks = new PTomBlocks();
	private PTomPet pet = new PTomPet();
	private PTomColor color = new PTomColor(1);
	private PTomColor color2 = new PTomColor(2);
	
	public PacketS2CTomProperties() {
		
	}
	
	public PacketS2CTomProperties(EntityTom tom) {
		this.entityId = tom.getEntityId();
		
		this.blocks = tom.favouriteBlocks;
		this.pet = tom.favouritePet;
		this.color = tom.favouriteColor;
		this.color2 = tom.favouriteColor2;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		
		this.blocks.fromBytes(buf);
		this.pet.fromBytes(buf);
		this.color.fromBytes(buf);
		this.color2.fromBytes(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		
		this.blocks.toBytes(buf);
		this.pet.toBytes(buf);
		this.color.toBytes(buf);
		this.color2.toBytes(buf);
	}
	
	public static class PacketS2CTomPropertiesHandler implements IMessageHandler<PacketS2CTomProperties, IMessage> {

		@Override
		public IMessage onMessage(PacketS2CTomProperties message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP player = minecraft.player;
			World world = player.world;
			
			Entity entity = world.getEntityByID(message.entityId);
			if(entity == null || entity.isDead || !(entity instanceof EntityTom))return null;
			
			EntityTom tom = (EntityTom)entity;
			tom.favouriteBlocks = message.blocks;
			tom.favouritePet = message.pet;
			tom.favouriteColor = message.color;
			tom.favouriteColor2 = message.color2;
			return null;
		}

	}
	
}
