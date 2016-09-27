package omg_utilities.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import omg_utilities.main.Main;
import omg_utilities.tileentity.LavaProviderTileEntity;

public class LavaProvider extends BlockContainer{

	public LavaProvider(String unlocalizedName){
		super(Material.ROCK);
		setUnlocalizedName(unlocalizedName);
		setCreativeTab(Main.OMGTabs);
		setHarvestLevel("pickaxe", 1);
		setHardness(3);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new LavaProviderTileEntity();
	}
	
	//Render
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
	   return true;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}

}
