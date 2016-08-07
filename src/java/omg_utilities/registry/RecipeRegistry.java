package omg_utilities.registry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeRegistry {
	public static void mainRegistry() {
		registerRecipe();
	}

	private static ItemStack spiralChest = new ItemStack(BlockRegistry.spiralChest, 1);
	
	private static void registerRecipe() {
		GameRegistry.addRecipe(new ShapedOreRecipe(spiralChest, true, new Object[]{"ABA", "B B", "ABA", 'A', "gemDiamond", 'B', "gemLapis"}));
	}
}
