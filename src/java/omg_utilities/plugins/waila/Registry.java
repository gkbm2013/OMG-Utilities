package omg_utilities.plugins.waila;

import net.minecraftforge.fluids.FluidStack;
import omg_utilities.blocks.LavaProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Registry {
	public static void wailaCallback(IWailaRegistrar registrar){
		
		registrar.addConfig("Tinker I/O", "tio.frozen", "Smart Output");
		registrar.registerBodyProvider(new LavaProviderInfo(), LavaProvider.class);
		
	}
	
    public static String fluidNameHelper (FluidStack f)
    {
        return f.getFluid().getName();
    }

}
