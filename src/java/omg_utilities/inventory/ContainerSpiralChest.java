package omg_utilities.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import omg_utilities.tileentity.SpiralChestTileEntity;

public class ContainerSpiralChest extends Container{
	
	public static final int INPUT = 0, STORAGE = 1, PRODUCT = 2;
	SpiralChestTileEntity tileSC;
	
	public ContainerSpiralChest(InventoryPlayer player, SpiralChestTileEntity tileEntitySC){
		tileSC = tileEntitySC;
		
		this.addSlotToContainer(new Slot(tileEntitySC, INPUT, 141, 21)); // input
		this.addSlotToContainer(new SlotUnuseable(tileEntitySC, STORAGE, 73, 58)); // storage
		this.addSlotToContainer(new SlotProduct(tileEntitySC, PRODUCT, 73, 142)); // product
		
		//player's inventory
		int i;
		for(i = 0; i < 3; ++i){
			for(int j = 0; j < 9; ++j){
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 164 + i * 18));
			}
		}
				
		//action bar
		for(i = 0; i < 9; ++i){
			this.addSlotToContainer(new Slot(player, i , 8 + i * 18 , 222));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tileSC.isUsableByPlayer(playerIn);
	}
	
	/**
	 * Called when a player shift-clicks on a slot.
	 */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		final int fimINV_SIZE = tileSC.getSizeInventory();
		ItemStack stack = ItemStack.EMPTY;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            if (slot < fimINV_SIZE) {
                    if (!this.mergeItemStack(stackInSlot, fimINV_SIZE, 36+fimINV_SIZE, false)) {
                            return ItemStack.EMPTY; //do nothing if it can't
                    }
            }
            //ItemStack is in player
            else {
            	//Any item is in player
            	if (!this.mergeItemStack(stackInSlot, INPUT, INPUT+1, false)){
        			return ItemStack.EMPTY;
        		}
            	// place in action bar
    			else if (slot < fimINV_SIZE+27) {
    				if (!this.mergeItemStack(stackInSlot, fimINV_SIZE+27, fimINV_SIZE+36, false)){
    					return ItemStack.EMPTY;
    				}
    			}
    			// item in action bar - place in player inventory
    			else if (slot >= fimINV_SIZE+27 && slot < fimINV_SIZE+36 ){
    				if (!this.mergeItemStack(stackInSlot, fimINV_SIZE, fimINV_SIZE+27, false)){
    					return ItemStack.EMPTY;
    				}
    			}
            }
            //places it into the tileEntity is possible since its in the player inventory
//            else if (!this.mergeItemStack(stackInSlot, 0, tileFIM.getSizeInventory(), false)) {
//                    return null;
//            }

                if (stackInSlot.getCount() == 0) {
                        slotObject.putStack(ItemStack.EMPTY);
                } else {
                        slotObject.onSlotChanged();
                }

                if (stackInSlot.getCount() == stack.getCount()) {
                        return ItemStack.EMPTY;
                }
                slotObject.onSlotChanged();
        }
        return stack;
    }
	
	/*@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		for(int i = 0; i < this.crafters.size(); ++i){
			ICrafting craft = (ICrafting) this.crafters.get(i);
			
			//if(this.lastInputTime != this.tileFIM.inputTime){
				//craft.sendProgressBarUpdate(this, 0, this.tileFIM.inputTime);
			//}
		}
		
		//this.lastInputTime = this.tileFIM.inputTime;
	}*/
	
	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
        /*for (int i = 0; i < this.inventorySlots.size(); ++i){
            ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);
            
            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)){
                itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                for (int j = 0; j < this.listeners.size(); ++j){
                    ((IContainerListener)this.listeners.get(j)).sendSlotContents(this, i, itemstack1);
                }
            }
        }*/
    }
}
