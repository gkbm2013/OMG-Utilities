package omg_utilities.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;

public abstract class TileEntityContainerAdapter extends TileEntity  implements ISidedInventory{
	
	protected ItemStack[] slots;
	protected String name;
	protected int stackLimit;
	
	public TileEntityContainerAdapter(String name, int slotsNum) {
		this(name, slotsNum, 64);
	}
	
	public TileEntityContainerAdapter(String name, int slotsNum, int stackLimit ) {
		this.name = name;
		this.slots = new ItemStack[slotsNum];
		this.stackLimit = stackLimit;
	}
	
	 /**
     * Get the name of this object.
     * For players this returns their username
     */
	@Override
	public String getName() {
		String langFileName = "tile." + this.getBlockType().getUnlocalizedName().substring(5) + ".name";
		return this.hasCustomName() ?
				this.name : I18n.translateToLocal(langFileName);
	}
	
	@Override
	public boolean hasCustomName() {
		return this.name != null && this.name.length() > 0;
	}
	
	/**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
	@Override
	public ITextComponent getDisplayName() {
		// TODO 
		return null;
	}
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return slots.length;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return this.stackLimit;
	}
	
	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.slots[index];
	}
	
	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.slots[index] != null) {
			ItemStack itemstack;
			if (this.slots[index].stackSize <= count) {
				itemstack = this.slots[index];
				this.slots[index] = null;
				return itemstack;
			} else {
				itemstack = this.slots[index].splitStack(count);

				if (this.slots[index].stackSize == 0) {
					this.slots[index] = null;
				}
				return itemstack;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public void clear() {
		  for(int i = 0; i < slots.length; i++) {
		      slots[i] = null;
		    }
	}
	
	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (this.slots[index] != null) {
			ItemStack itemstack = this.slots[index];
			this.slots[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the given item stack to the specified slot in the inventory
	 * (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(
			int slot, ItemStack itemstack) {
		this.slots[slot] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	/**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false :
			player.getDistanceSq(this.pos.add(0.5D, 0.5D, 0.5D)) <= 64.0D;
	}
	
	@Override public void openInventory(EntityPlayer player) {}
	@Override public void closeInventory(EntityPlayer player) {}
	@Override public int getField(int id) {return 0;}
	@Override public void setField(int id, int value) {}
	@Override public int getFieldCount() {return 0;}
	
	public ItemStack[] getSlots() {
		return slots;
	}
	
	/**
	 *  loading & saving
	 */
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		readItems(nbt);
		readName(nbt);
	}
	
	private void readItems(NBTTagCompound nbt)
	{
		NBTTagList tagList = nbt.getTagList("Items", 10);
		slots = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbt1 = tagList.getCompoundTagAt(i);
			byte b = nbt1.getByte("Slot");

			if (b >= 0 && b < this.slots.length) {
				slots[b] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
	}
	
	private void readName(NBTTagCompound nbt)
	{
		if (nbt.hasKey("CustomName", 8)) {
			this.name = nbt.getString("CustomName");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		writeItems(nbt);
		writeName(nbt);
		return nbt;
	}
	
	private void writeItems(NBTTagCompound nbt)
	{
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < slots.length; ++i) {
			if (slots[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte) i);
				slots[i].writeToNBT(nbt1);
				tagList.appendTag(nbt1);
			}
		}
		nbt.setTag("Items", tagList);
	}
	
	private void writeName(NBTTagCompound nbt)
	{
		if (this.hasCustomName()) {
			nbt.setString("CustomName", this.name);
		}
	}
}

