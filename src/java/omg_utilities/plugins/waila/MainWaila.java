package omg_utilities.plugins.waila;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import omg_utilities.main.Main;

@ObjectHolder(Main.MODID)
public class MainWaila {
	public static void startPlugin() {
		load();
	}

	private static void load() {
		FMLLog.getLogger().info("[OMG Utilities] OMG Utilities Plugin for Waila Started!");
        FMLInterModComms.sendMessage("Waila", "register", "omg_utilities.plugins.waila.Registry.wailaCallback");		
	}
}
