package omg_utilities.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import omg_utilities.handler.GuiHandler;
import omg_utilities.main.Main;
import omg_utilities.tileentity.SpiralChestTileEntity;

public class CommonProxy {
	
	public void registerTileEntities(){
		GameRegistry.registerTileEntity(SpiralChestTileEntity.class, Main.MODID+"TileEntitySC");
	}
	
	public void registerNetworkStuff(){
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		
	}

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		 return ctx.getServerHandler().playerEntity;
	}
}
