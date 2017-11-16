package omg_utilities.registry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import omg_utilities.blocks.LavaProvider;
import omg_utilities.blocks.SpiralChest;
import omg_utilities.main.Main;

public class RegisterUtil {
	
	public static Block spiralChest;
	public static Block lavaProvider;
	
	private static void preLoad() {
		spiralChest = new SpiralChest("spiral_chest");
		lavaProvider = new LavaProvider("lava_provider");
	}
	
	public static void registerAll(FMLPreInitializationEvent event){
		preLoad();
		registerBlocks(event,
				spiralChest,
				lavaProvider);
	}
	
	static void registerBlocks(FMLPreInitializationEvent event, Block... blocks){
		for(Block block : blocks){
			block.setRegistryName(block.getUnlocalizedName().substring(5));
			ForgeRegistries.BLOCKS.register(block);
			final ItemBlock itemblock = new ItemBlock(block);
			itemblock.setRegistryName(block.getRegistryName());
			ForgeRegistries.ITEMS.register(itemblock);
			if(event.getSide() == Side.CLIENT){
				ModelResourceLocation rsl = getModelResourceLocation(block.getUnlocalizedName().substring(5));
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, rsl);
			}
		}
	}
	
	static void registerItems(FMLPreInitializationEvent event, Item... items){
		for(Item item : items){
			if(item.getRegistryName() == null){
				item.setRegistryName(item.getUnlocalizedName().substring(5));
			}
			ForgeRegistries.ITEMS.register(item);
			if(event.getSide() == Side.CLIENT){
				if(item.getHasSubtypes()){
					NonNullList<ItemStack> list = NonNullList.create();
					list.add(new ItemStack(item));
					item.getSubItems(Main.OMGTabs, list);
					for(int i = 0; i < list.size(); i++){
						ModelResourceLocation rsl = getModelResourceLocation(item.getUnlocalizedName().substring(5)+"_"+i);
						ModelLoader.setCustomModelResourceLocation(item, i, rsl);
					}
				}else{
					//ModelResourceLocation rsl = getModelResourceLocation(item.getUnlocalizedName().substring(5));
					ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
				}
			}
		}
	}
	
	private static ModelResourceLocation getModelResourceLocation(String name) {
		return new ModelResourceLocation(Main.MODID + ":" + name, "inventory");
	}
}
