package omg_utilities.registry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeRegistry {
	public static void mainRegistry() {
		registerRecipe();
	}

	private static ItemStack spiralChest = new ItemStack(BlockRegistry.spiralChest, 1);
	
	private static ItemStack lavaProvider = new ItemStack(BlockRegistry.lavaProvider, 1);
	private static ItemStack blazeRod = new ItemStack(Items.BLAZE_ROD, 1);
	private static ItemStack enderPeral = new ItemStack(Items.ENDER_PEARL, 1);
	private static ItemStack netherBrick = new ItemStack(Items.NETHERBRICK, 1);
	
	private static void registerRecipe() {
		GameRegistry.addRecipe(new ShapedOreRecipe(spiralChest, true, new Object[]{"ABA", "B B", "ABA", 'A', "gemDiamond", 'B', "gemLapis"}));
		GameRegistry.addRecipe(spiralChest, "A", 'A', BlockRegistry.spiralChest);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(lavaProvider, true, new Object[]{"ABA", "BDB", "CBC", 'A', netherBrick, 'B', blazeRod, 'C', enderPeral, 'D', "blockGold"}));
	}
}
