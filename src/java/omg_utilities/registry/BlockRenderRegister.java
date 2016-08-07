package omg_utilities.registry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import omg_utilities.main.Main;

public class BlockRenderRegister {
	public static String modid = Main.MODID;
	
	public static void registerBlockRenderer() {
		reg(BlockRegistry.spiralChest);
	}
	
	public static void reg(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
