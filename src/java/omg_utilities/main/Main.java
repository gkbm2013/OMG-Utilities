package omg_utilities.main;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import omg_utilities.plugins.waila.MainWaila;
import omg_utilities.proxy.CommonProxy;
import omg_utilities.registry.BlockRegistry;
import omg_utilities.registry.RecipeRegistry;

@Mod(
		modid = Main.MODID,
		version = Main.VERSION,
		name = Main.Name,
		dependencies="required-after:Forge@[12.18.1.2073,);"
				+ "after:JEI;"
				+ "after:Waila;",
		acceptedMinecraftVersions = "[1.10.2,]")
public class Main {
    public static final String MODID = "omg_utilities";
    public static final String VERSION = "alpha 0.1.0";
    public static final String Name = "OMG Utilities";
    
    //Proxy
    @SidedProxy(modId=Main.MODID, clientSide="omg_utilities.proxy.ClientProxy", serverSide="omg_utilities.proxy.ServerProxy")
	public static CommonProxy proxy;
    
    //MOD
    @Instance(Main.MODID)
    public static Main instance;
    
    //Creative Tabs
    public static CreativeTabs OMGTabs = new CreativeTabs("OMG_Tabs"){
    	public Item getTabIconItem() {
    		return new ItemStack(BlockRegistry.spiralChest).getItem();
    	}
    };
        
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.preInit(event);
		BlockRegistry.mainRegistry();
		proxy.registerTileEntities();
		
		if (Loader.isModLoaded("Waila")) {
            FMLLog.getLogger().info("[OMG Utilities] Start loading OMG Utilities plugin for Waila.");
            MainWaila.startPlugin();
        }else{
        	FMLLog.getLogger().info("[OMG Utilities] OMG Utilities plugin for Waila skiped.");
        }
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent event){
		proxy.init(event);
		RecipeRegistry.mainRegistry();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
		proxy.registerNetworkStuff();
	}
}
