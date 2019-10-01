package kaptainwutax.traders.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import kaptainwutax.traders.util.Product;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ConfigTomNeeds extends Config {

	private static String[] COLOR_NAMES = {"white", "orange", "magenta", "light_blue", "yellow", 
											"lime", "pink", "gray", "light_gray","cyan", 
											"purple", "blue", "brown", "green", "red","black"};
	
	@Expose public List<Product> FAVOURITE_BLOCKS = new ArrayList<>();
	@Expose public List<String> FAVOURITE_PETS = new ArrayList<>();
	@Expose public List<List<Product>> FAVOURITE_COLORS = new ArrayList<>();
	
	@Override
	public String getLocation() {
		return "tom_needs.json";
	}

	@Override
	protected void resetConfig() {
		this.FAVOURITE_BLOCKS.add(new Product(Item.getItemFromBlock(Blocks.COBBLESTONE), 0, 0, null));
		this.FAVOURITE_BLOCKS.add(new Product(Item.getItemFromBlock(Blocks.STONE), 0, 0, null));
		this.FAVOURITE_BLOCKS.add(new Product(Item.getItemFromBlock(Blocks.LOG), 0, 0, null));
		this.FAVOURITE_BLOCKS.add(new Product(Item.getItemFromBlock(Blocks.PLANKS), 0, 0, null));
		this.FAVOURITE_BLOCKS.add(new Product(Item.getItemFromBlock(Blocks.BRICK_BLOCK), 0, 0, null));
		this.FAVOURITE_BLOCKS.add(new Product(Item.getItemFromBlock(Blocks.STONEBRICK), 0, 0, null));
		this.FAVOURITE_BLOCKS.add(new Product(Item.getItemFromBlock(Blocks.LEAVES), 0, 0, null));
		
		for(ResourceLocation entityName: EntityList.getEntityNameList()) {
			if(!entityName.getResourceDomain().equals("minecraft"))continue;
			
			Class<? extends Entity> entityClass = EntityList.getClass(entityName);

			if(entityClass != null && EntityAnimal.class.isAssignableFrom(entityClass)) {
				this.FAVOURITE_PETS.add(entityName.toString());
			}
		}
		
		Block[] blocks = new Block[] {
				Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA,
				Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA,
				Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.SILVER_GLAZED_TERRACOTTA,
				Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA,
				Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA,
				Blocks.BLACK_GLAZED_TERRACOTTA
		};
		
		for(int i = 0; i < COLOR_NAMES.length; i++) {
			List<Product> coloredBlocks = new ArrayList<>();
			coloredBlocks.add(new Product(Item.getItemFromBlock(Blocks.WOOL), i, 0, null));
			coloredBlocks.add(new Product(Item.getItemFromBlock(Blocks.CONCRETE), i, 0, null));
			coloredBlocks.add(new Product(Item.getItemFromBlock(Blocks.CONCRETE_POWDER), i, 0, null));
			coloredBlocks.add(new Product(Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY), i, 0, null));
			coloredBlocks.add(new Product(Item.getItemFromBlock(Blocks.CARPET), i, 0, null));
			coloredBlocks.add(new Product(Item.getItemFromBlock(Blocks.STAINED_GLASS), i, 0, null));
			coloredBlocks.add(new Product(Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE), i, 0, null));
			coloredBlocks.add(new Product(Item.getItemFromBlock(blocks[i]), 0, 0, null));
			this.FAVOURITE_COLORS.add(coloredBlocks);
		}
	}

}
