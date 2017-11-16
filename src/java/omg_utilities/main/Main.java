package omg_utilities.main;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import omg_utilities.config.OMGConfig;
import omg_utilities.plugins.waila.MainWaila;
import omg_utilities.proxy.CommonProxy;
import omg_utilities.registry.RecipeRegistry;
import omg_utilities.registry.RegisterUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = Main.MODID,
		version = Main.VERSION,
		name = Main.Name,
		dependencies="required-after:forge@[14.21.1.2410,);"
				+ "after:jei;"
				+ "after:waila;",
		acceptedMinecraftVersions = "[1.12,]")
public class Main {
    public static final String MODID = "omg_utilities";
    public static final String VERSION = "beta 1.1.0";
    public static final String Name = "OMG Utilities";
    
    //Proxy
    @SidedProxy(modId=Main.MODID, clientSide="omg_utilities.proxy.ClientProxy", serverSide="omg_utilities.proxy.ServerProxy")
	public static CommonProxy proxy;
    
    //MOD
    @Instance(Main.MODID)
    public static Main instance;
    
    public static Configuration config;
    public static Logger logger = LogManager.getLogger(Main.Name);
    
    //Creative Tabs
    public static CreativeTabs OMGTabs = new CreativeTabs("OMG_Tabs"){
    	public ItemStack getTabIconItem() {
    		return new ItemStack(RegisterUtil.spiralChest);
    	}
    };
        
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		config = new Configuration(event.getSuggestedConfigurationFile());
		OMGConfig.syncConfig();
		proxy.preInit(event);
		RegisterUtil.registerAll(event);
		proxy.registerTileEntities();
		
		if (Loader.isModLoaded("Waila")) {
			logger.info("[OMG Utilities] Start loading OMG Utilities plugin for Waila.");
            MainWaila.startPlugin();
        }else{
        	logger.info("[OMG Utilities] OMG Utilities plugin for Waila skiped.");
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
