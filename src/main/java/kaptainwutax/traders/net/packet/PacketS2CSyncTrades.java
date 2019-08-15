package kaptainwutax.traders.net.packet;

import java.io.IOException;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import kaptainwutax.traders.entity.EntityTrader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketS2CSyncTrades implements IMessage {
	
	private int entityId;
	private MerchantRecipeList trades;

	public PacketS2CSyncTrades() {
		
	}
	
	public PacketS2CSyncTrades(int entityId, MerchantRecipeList trades) {
		this.entityId = entityId;
		this.trades = trades;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.trades = this.readTrades(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {	
		buf.writeInt(this.entityId);
		this.writeTrades(buf);
	}
	
    public MerchantRecipeList readTrades(ByteBuf buffer) {
        MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        int i = buffer.readByte() & 255;

        for(int j = 0; j < i; ++j) {
            ItemStack itemstack = this.readItemStack(buffer);
            ItemStack itemstack1 = this.readItemStack(buffer);
            ItemStack itemstack2 = ItemStack.EMPTY;

            if (buffer.readBoolean()) {
                itemstack2 = this.readItemStack(buffer);
            }

            boolean flag = buffer.readBoolean();
            int k = buffer.readInt();
            int l = buffer.readInt();
            MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1, k, l);

            if(flag) {
                merchantrecipe.compensateToolUses();
            }

            merchantrecipelist.add(merchantrecipe);
        }

        return merchantrecipelist;
    }
    
    public ItemStack readItemStack(ByteBuf buffer) {
        int i = buffer.readShort();

        if (i < 0) {
            return ItemStack.EMPTY;
        } else {
            int j = buffer.readByte();
            int k = buffer.readShort();
            ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
            itemstack.getItem().readNBTShareTag(itemstack, this.readCompoundTag(buffer));
            return itemstack;
        }
    }
    
    @Nullable
    public NBTTagCompound readCompoundTag(ByteBuf buffer) {
        int i = buffer.readerIndex();
        byte b0 = buffer.readByte();

        if (b0 == 0) {
            return null;
        } else {
        	buffer.readerIndex(i);

            try {
                return CompressedStreamTools.read(new ByteBufInputStream(buffer), new NBTSizeTracker(2097152L));
            } catch(IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }
	

	public void writeTrades(ByteBuf buffer) {
        buffer.writeByte((byte)(this.trades.size() & 255));

        for(int i = 0; i < this.trades.size(); ++i) {
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.trades.get(i);
            this.writeItemStack(merchantrecipe.getItemToBuy(), buffer);
            this.writeItemStack(merchantrecipe.getItemToSell(), buffer);
            ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
            buffer.writeBoolean(!itemstack.isEmpty());

            if (!itemstack.isEmpty()) {
                writeItemStack(itemstack, buffer);
            }

            buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
            buffer.writeInt(merchantrecipe.getToolUses());
            buffer.writeInt(merchantrecipe.getMaxTradeUses());
        }
    }
    
    public void writeItemStack(ItemStack stack, ByteBuf buffer) {
        if (stack.isEmpty()) {
        	buffer.writeShort(-1);
        } else {
        	buffer.writeShort(Item.getIdFromItem(stack.getItem()));
        	buffer.writeByte(stack.getCount());
        	buffer.writeShort(stack.getMetadata());
            NBTTagCompound nbttagcompound = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                nbttagcompound = stack.getItem().getNBTShareTag(stack);
            }

            this.writeCompoundTag(nbttagcompound, buffer);
        }
    }
    
    public void writeCompoundTag(@Nullable NBTTagCompound nbt, ByteBuf buffer) {
        if (nbt == null) {
        	buffer.writeByte(0);
        } else {
            try {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(buffer));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }
	
	
	public static class PacketS2CSyncTradesHandler implements IMessageHandler<PacketS2CSyncTrades, IMessage> {

		@Override
		public IMessage onMessage(PacketS2CSyncTrades message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP player = minecraft.player;
			World world = player.world;
			
			Entity entity = world.getEntityByID(message.entityId);
			if(entity == null || entity.isDead || !(entity instanceof EntityTrader))return null;
			
			EntityTrader trader = (EntityTrader)entity;
			trader.setTrades(message.trades);
			return null;
		}

	}
	
}
