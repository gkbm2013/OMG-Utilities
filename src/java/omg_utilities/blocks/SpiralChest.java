package omg_utilities.blocks;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import omg_utilities.main.Main;
import omg_utilities.tileentity.SpiralChestTileEntity;

public class SpiralChest extends BlockContainer{
	public SpiralChest(String unlocalizedName){
		super(Material.ROCK);
		setUnlocalizedName(unlocalizedName);
		setCreativeTab(Main.OMGTabs);
		setHarvestLevel("pickaxe", 1);
		setHardness(3);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new SpiralChestTileEntity();
	}
	
	@Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
	    if(playerIn.isSneaking()) {
	        return false;
	      }
		if (!worldIn.isRemote) {
		    playerIn.openGui(Main.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
        return true;
    }
	
	@Override
	 public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		
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
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		if(worldIn.isRemote){
			return;
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null){
			SpiralChestTileEntity tile = (SpiralChestTileEntity) te;
			if(stack.getTagCompound() != null){
				NBTTagCompound nbt = (NBTTagCompound) stack.getTagCompound().getTag("nbtTag");
				if(nbt != null){
					int storageAmount = nbt.getInteger("storageAmount");
					int tier = nbt.getInteger("tier");
					int timer = nbt.getInteger("timer");
					int speed = nbt.getInteger("speed");
					String itemName = nbt.getString("itemName");
					
					tile.setValue(storageAmount, tier, timer, speed, itemName);
				}
			}
		}
    }
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if(world.isRemote){
			 return false;
		 }
		
	    //IBlockState state = world.getBlockState(pos);
	    this.onBlockDestroyedByPlayer(world, pos, state);
	    if(willHarvest) {
	      this.harvestBlock(world, player, pos, state, world.getTileEntity(pos), null);
	    }

	    world.setBlockToAir(pos);
	    return false;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		
	    List<ItemStack> items = super.getDrops(world, pos, state, fortune);
	    
	    TileEntity te = world.getTileEntity(pos);
	    
	    if(te != null && te instanceof SpiralChestTileEntity) {
	    	SpiralChestTileEntity tile = (SpiralChestTileEntity) te;
	    	for(ItemStack item : items) {
		        if(item.getItem() == Item.getItemFromBlock(this)) {
		        	NBTTagCompound nbt = new NBTTagCompound();
		            NBTTagCompound tag = tile.writeToNBT(new NBTTagCompound());
		            if(tile.getSlots()[1] != null){
		            	tag.setString("itemName", tile.getSlots()[1].getItem().getRegistryName().toString());
		            }
		            nbt.setTag("nbtTag", tag);
		            item.setTagCompound(nbt);
		        }
		      }
	    }
	    return items;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if(worldIn.isRemote){
			return;
		}
	    TileEntity tile = worldIn.getTileEntity(pos);

	    if(tile instanceof IInventory) {
	    	IInventory inventory = (IInventory)tile;
	    	inventory.removeStackFromSlot(1);
	    	InventoryHelper.dropInventoryItems(worldIn, pos, inventory);
	    	worldIn.updateComparatorOutputLevel(pos, this);
	    }
	    super.breakBlock(worldIn, pos, state);
	}
}
