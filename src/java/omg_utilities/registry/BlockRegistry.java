package omg_utilities.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import omg_utilities.blocks.LavaProvider;
import omg_utilities.blocks.SpiralChest;
import omg_utilities.main.Main;

public class BlockRegistry {
	public static void mainRegistry() {
		preLoadBlock();
		registerBlock();
	}
	
	public static Block spiralChest;
	public static Block lavaProvider;
	
	private static void preLoadBlock() {
		spiralChest = new SpiralChest("spiral_chest");
		lavaProvider = new LavaProvider("lava_provider");
	}
	
	private static void registerBlock() {
		registerBlock(spiralChest, "spiral_chest");
		registerBlock(lavaProvider, "lava_provider");
	}
	
    public static <K extends IForgeRegistryEntry<?>> K  registerBlock(K object, String name){
    	ResourceLocation rsl = new ResourceLocation(Main.MODID, name);
    	object.setRegistryName(rsl);
    	GameRegistry.register(object);
    	GameRegistry.register(new ItemBlock((Block) object).setRegistryName(object.getRegistryName()));
    	return object;
    }
}
