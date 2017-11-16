package omg_utilities.registry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import omg_utilities.main.Main;

public class RecipeRegistry {
	public static void mainRegistry() {
		registerRecipe();
	}
	
	static ResourceLocation recipeGroup = new ResourceLocation(Main.MODID);
	
	private static ItemStack spiralChest = new ItemStack(RegisterUtil.spiralChest, 1);
	
	private static ItemStack lavaProvider = new ItemStack(RegisterUtil.lavaProvider, 1);
	private static ItemStack blazeRod = new ItemStack(Items.BLAZE_ROD, 1);
	private static ItemStack enderPeral = new ItemStack(Items.ENDER_PEARL, 1);
	private static ItemStack netherBrick = new ItemStack(Items.NETHERBRICK, 1);
	
	private static void registerRecipe() {
		ForgeRegistries.RECIPES.register(new ShapedOreRecipe(recipeGroup, spiralChest, new Object[]{"ABA", "B B", "ABA", 'A', "gemDiamond", 'B', "gemLapis"}).setRegistryName("spiralChest"));
		ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(recipeGroup, spiralChest, RegisterUtil.spiralChest).setRegistryName("spiralChest_reset"));
		
		ForgeRegistries.RECIPES.register(new ShapedOreRecipe(recipeGroup, lavaProvider, new Object[]{"ABA", "BDB", "CBC", 'A', netherBrick, 'B', blazeRod, 'C', enderPeral, 'D', "blockGold"}).setRegistryName("lavaProvider"));
	}
}
