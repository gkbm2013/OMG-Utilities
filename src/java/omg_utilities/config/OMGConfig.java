package omg_utilities.config;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import omg_utilities.main.Main;

public class OMGConfig {
	
	public static int[] spiral_chest_level = {100, 1000, 10000, 100000, 1000000};
	
	public static void syncConfig(){
		Configuration config = Main.config;
		try {
			// Load config
			config.load();
			// Read props from config
			for(int i = 0; i < 5; i++){
				Property level = config.get(Configuration.CATEGORY_GENERAL,
		                "spiral_chest_level_" + (i+1) + "_amount",
		                String.valueOf(spiral_chest_level[i]),
		                "The item required for Spiral Chest level " + (i+1) + ".");
				spiral_chest_level[i] = level.getInt();
			}
	        
	    } catch (Exception e) {
	    	Main.logger.log(Level.ERROR, "Config setting error! %d", e);
	    } finally {
	        if (config.hasChanged()){
	        	config.save();
	        	Main.logger.log(Level.INFO, "Config successfully reloaded!", "");
	        }
	    }
	}
}
