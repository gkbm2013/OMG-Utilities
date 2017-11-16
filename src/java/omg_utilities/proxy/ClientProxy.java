package omg_utilities.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy{
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
	 // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
	 // your packets will not work because you will be getting a client
	 // player even when you are on the server! Sounds absurd, but it's true.

	 // Solution is to double-check side before returning the player:
	 return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		//BlockRenderRegister.registerBlockRenderer();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		
	}
}
