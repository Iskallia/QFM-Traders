package kaptainwutax.traders.util;

import java.util.List;
import java.util.Random;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ServerChat {
	
	private static Random RAND = new Random();
	
	public static void whisper(EntityPlayerMP playerMP, Entity sender, List<String> phrases) {
		if(phrases.size() <= 0)return;
		whisper(playerMP, sender, phrases.get(RAND.nextInt(phrases.size())));	
	}

	public static void whisper(EntityPlayerMP playerMP, Entity sender, String message) {
		try {
			message = message.replace("<username>", playerMP.getName());
			
			ITextComponent messageComponent = CommandBase.getChatComponentFromNthArg(
					sender, new String[] {message}, 0, !(playerMP instanceof EntityPlayer)
			);
            
			TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(
            		"commands.message.display.incoming", 
            		new Object[] {sender.getDisplayName(), messageComponent.createCopy()}            
			);
			
            textcomponenttranslation.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.valueOf(true));
            playerMP.sendMessage(textcomponenttranslation);
		} catch(CommandException e) {
			e.printStackTrace();
		}
	}

}
