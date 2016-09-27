package omg_utilities.tileentity;

import java.math.BigDecimal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpiralChestTileEntity extends TileEntityContainerAdapter implements ITickable{
	public SpiralChestTileEntity() {
		super(null, 3);
	}
	
	private final int[] slotsInput = new int[] { 0 };
	private final int[] slotsStorage = new int[] { 1 };
	private final int[] slotsProduct = new int[] { 2 };
	
	private String nameSpiralChest;
	
	private int storageAmount;
	private int timeout = 144000;
	private int tier;
	private int speed;
	private int timer;
	
	public void nameSpiralChest(String string){
		this.nameSpiralChest = string;
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.nameSpiralChest : I18n.translateToLocal("tile.spiral_chest.name");
	}

	@Override
	public boolean hasCustomName() {
		return this.nameSpiralChest != null && this.nameSpiralChest.length() > 0;
	}

	//Slot
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] slot = null;
		if(side == EnumFacing.DOWN){
			slot = this.slotsProduct;
		}else{
			slot = this.slotsInput;
		}
		return slot;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if(index == 2){
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack) {
		return true;
	}
	
	//Process
	@Override
	public void update() {
		if (worldObj.isRemote) return;
		inputItem();
		produce();
		notifyBlockUpdate();
	}
	
	private void inputItem(){
		boolean input = false;
		if(this.getSlots()[0] != null){
			if(this.getSlots()[1] != null && this.getSlots()[1].isItemEqual(this.getSlots()[0])){
				input = true;
			}else if(this.getSlots()[1] == null){
				this.getSlots()[1] = this.getSlots()[0].copy();
				this.getSlots()[1].stackSize = 1;
				input = true;
			}
			
			if(input && this.storageAmount < 2147483647){
				this.storageAmount++;
				if(getSlots()[0].stackSize == 1){
					getSlots()[0] = null;
				}else{
					--getSlots()[0].stackSize;
				}
				calculatingTeir();
			}
		}
	}
	
	private void calculatingTeir(){
		if(this.storageAmount >= 100 && this.storageAmount < 999){
			this.tier = 1;
			this.speed = 1;
		}else if(this.storageAmount >= 1000 && this.storageAmount < 9999){
			this.tier = 2;
			this.speed = 12;
		}else if(this.storageAmount >= 10000 && this.storageAmount < 99999){
			this.tier = 3;
			this.speed = 120;
		}else if(this.storageAmount >= 100000 && this.storageAmount < 999999){
			this.tier = 4;
			this.speed = 1440;
		}else if(this.storageAmount >= 1000000){
			this.tier = 5;
			this.speed = 144000;
		}
	}
	
	private boolean canProduce(){
		if(this.getSlots()[1] != null && tier > 0){
			if(this.getSlots()[2] == null){
				return true;
			}else if(this.getSlots()[2] != null && this.getSlots()[1].isItemEqual(this.getSlots()[2]) && this.getSlots()[2].stackSize < this.getSlots()[2].getMaxStackSize()){
				return true;
			}
		}
		return false;
	}
	
	private void produce(){
		if(canProduce()){
			if(this.timer >= this.timeout){
				if (this.getSlots()[2] == null) {
					this.getSlots()[2] = this.getSlots()[1].copy();
				} else if (this.getSlots()[2].getItem() == this.getSlots()[1].getItem()) {
					this.getSlots()[2].stackSize ++;
				}
				timer = 0;
			}else{
				this.timer += this.speed;
			}
		}
	}
	
	public void setValue(int storageAmount, int tier, int timer, int speed, String itemName){
		if(this.getSlots()[1] == null){
			Item item = Item.getByNameOrId(itemName);
			if(item != null){
				ItemStack itemstack = new ItemStack(item, 1);
				this.getSlots()[1] = itemstack;
				this.storageAmount = storageAmount;
				this.tier = tier;
				this.timer = timer;
				this.speed = speed;
			}
		}
	}
	
	//GUI
	@SideOnly(Side.CLIENT)
	public int getStorageAmount(){
		return this.storageAmount;
	}
	
	@SideOnly(Side.CLIENT)
	public int getTier(){
		return this.tier;
	}
	
	@SideOnly(Side.CLIENT)
	public int getPrec(){
		double rate = (double) this.timer / (double) this.timeout;
		rate = new BigDecimal(rate)
				.setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		rate = rate * 100;
		return (int) rate;
	}
	
	//NBT
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.storageAmount = tagCompound.getInteger("storageAmount");
		this.tier = tagCompound.getInteger("tier");
		this.timer = tagCompound.getInteger("timer");
		this.speed = tagCompound.getInteger("speed");
		
		if (tagCompound.hasKey("CustomName", 8)) {
			this.nameSpiralChest = tagCompound.getString("CustomName");
		}
		
		NBTTagList nbttaglist = tagCompound.getTagList("Items", 10);
        this.slots = new ItemStack[this.getSizeInventory()];


        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");

            if (j >= 0 && j < this.slots.length)
            {
                this.slots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
	}
		
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("storageAmount", this.storageAmount);
		tagCompound.setInteger("tier", this.tier);
		tagCompound.setInteger("timer", this.timer);
		tagCompound.setInteger("speed", this.speed);

		NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.slots.length; ++i)
        {
            if (this.slots[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.slots[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        tagCompound.setTag("Items", nbttaglist);
		
		return tagCompound;
	}
		
	//Packet
	 @Override
	 public SPacketUpdateTileEntity getUpdatePacket() {	 
	     NBTTagCompound tag = new NBTTagCompound();
	     this.writeToNBT(tag);
	     return new SPacketUpdateTileEntity(pos, 1, tag);
	 }
		 
	 @Override
	 public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
	     readFromNBT(packet.getNbtCompound());
	 }
	 
	private void notifyBlockUpdate(){
		if(worldObj!=null && pos != null){
			IBlockState state = worldObj.getBlockState(pos);
			worldObj.notifyBlockUpdate(pos, state, state, 3);
		}
	}
}
