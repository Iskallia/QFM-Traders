package kaptainwutax.traders.event.client;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import kaptainwutax.traders.Traders;
import kaptainwutax.traders.util.Time;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Traders.MOD_ID, value = Side.CLIENT)
public class EventRender {
	
	private static Minecraft MINECRAFT = Minecraft.getMinecraft();
	public static String[] DAY_NAMES = new String[] {
			"Sun.",
			"Mon.",
			"Tue.",
			"Wed.",
			"Thu.",
			"Fri.",
			"Sat."
	};
	
	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent event) {	
		if(event.getType() != ElementType.SUBTITLES)return;
		Time.updateTime(MINECRAFT.world.getTotalWorldTime());
		
		String time = DAY_NAMES[Time.DAY_OF_WEEK] + " " 
				+ Time.HOUR_OF_DAY +
				(Time.MINUTE_OF_HOUR < 10 ? ":0" : ":") + Time.MINUTE_OF_HOUR 
				+ " (Week " + Time.WEEK + ")";
		drawString(time, Color.WHITE.getRGB());
	}
	
	public static void drawString(String text, int color) {
		FontRenderer fontRender = MINECRAFT.fontRenderer;
		ScaledResolution scaled = new ScaledResolution(MINECRAFT);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();	

		int stringWidth = fontRender.getStringWidth(text);		
		
		GL11.glPushMatrix();
		fontRender.drawStringWithShadow(text, (int)10, 10, color);
		GL11.glPopMatrix();		
	}	
	
}
