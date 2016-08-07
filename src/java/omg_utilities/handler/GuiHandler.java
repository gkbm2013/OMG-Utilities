package omg_utilities.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import omg_utilities.gui.SpiralChestGui;
import omg_utilities.inventory.ContainerSpiralChest;
import omg_utilities.tileentity.SpiralChestTileEntity;

public class GuiHandler implements IGuiHandler {
	public GuiHandler (){
		
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0){
			SpiralChestTileEntity tileSC = (SpiralChestTileEntity) world.getTileEntity(new BlockPos(x, y, z));
			return new ContainerSpiralChest(player.inventory, tileSC);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0){
			SpiralChestTileEntity tileOCContainer = (SpiralChestTileEntity) world.getTileEntity(new BlockPos(x, y, z));
			return new SpiralChestGui(player.inventory, tileOCContainer);
		}
		return null;
	}
}
