package kaptainwutax.traders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.traders.entity.EntityTom;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketS2CTomHappiness implements IMessage {
	
	private int entityId;
	
	private int happiness;	
	private int food;
	private boolean hungerEffect;
	private float health;
	
	public PacketS2CTomHappiness() {
		
	}
	
	public PacketS2CTomHappiness(int happiness, int food, boolean hungerEffect, float health, EntityTrader trader) {
		this.happiness = happiness;
		this.food = food;
		this.hungerEffect = hungerEffect;
		this.health = health;
		this.entityId = trader.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.happiness = buf.readInt();
		this.food = buf.readInt();
		this.hungerEffect = buf.readBoolean();
		this.health = buf.readFloat();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.happiness);
		buf.writeInt(this.food);
		buf.writeBoolean(this.hungerEffect);
		buf.writeFloat(this.health);
	}

	public static class PacketS2CTomHappinessHandler implements IMessageHandler<PacketS2CTomHappiness, IMessage> {

		@Override
		public IMessage onMessage(PacketS2CTomHappiness message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP player = minecraft.player;
			World world = player.world;
			
			Entity entity = world.getEntityByID(message.entityId);
			if(entity == null || entity.isDead || !(entity instanceof EntityTom))return null;
			
			EntityTom tom = (EntityTom)entity;
			tom.setHappiness(message.happiness);
			tom.getFoodStats().setFoodLevel(message.food);
			tom.setHealth(message.health);
			if(message.hungerEffect)tom.addPotionEffect(new PotionEffect(MobEffects.HUNGER));
			else tom.removePotionEffect(MobEffects.HUNGER);
			return null;
		}

	}

}

