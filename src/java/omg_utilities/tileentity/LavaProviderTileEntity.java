package omg_utilities.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class LavaProviderTileEntity extends TileEntity implements IFluidHandler, IFluidTank, ITickable{
	
	private FluidTank tank;
	
	public LavaProviderTileEntity(){
		tank = new FluidTank(Fluid.BUCKET_VOLUME * 50);
	}
	
	int clock = 0;
	FluidStack lavaStack = new FluidStack(FluidRegistry.LAVA, 500);
	
	public FluidTank getTank(){
		return tank;
	}
	
	@Override
	public void update() {
		if(worldObj.isRemote){return;}
		if(this.getInfo().fluid == null || this.getInfo().fluid.amount < this.getInfo().capacity){
			if(clock >= 20){
				this.fill(lavaStack, true);
				notifyBlockUpdate();
				clock = 0;
			}else{
				clock++;
			}
		}
	}

	@Override
	public FluidStack getFluid() {
		return tank.getFluid();
	}

	@Override
	public int getFluidAmount() {
		if(tank.getFluid() != null){
			return tank.getFluid().amount;
		}else{
			return 0;
		}
	}

	@Override
	public int getCapacity() {
		return tank.getCapacity();
	}

	@Override
	public FluidTankInfo getInfo() {
		return this.tank.getInfo();
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return this.tank.getTankProperties();
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	/**
	 * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
	 */
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		//TODO
		if(!resource.isFluidEqual(lavaStack)){return 0;}
		int amount = tank.fill(resource, doFill);
		this.notifyBlockUpdate();
        return amount;
	}

	/**
	 * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
	 */
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tank.getFluidAmount() == 0)
            return null;
        if (tank.getFluid().getFluid() != resource.getFluid())
            return null;
        this.notifyBlockUpdate();
		return this.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		FluidStack amount = tank.drain(maxDrain, doDrain);
        if (amount != null && doDrain)
        {
        	this.notifyBlockUpdate();
        }
        return amount;
	}
	
	@SuppressWarnings("unchecked")
	@Nonnull
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tank;
		}
		return super.getCapability(capability, facing);
	}
	
	//NBT
	@Override
	public void readFromNBT (NBTTagCompound tags){
		super.readFromNBT(tags);
		
		if(tags.getBoolean("hasFluid")){
			FluidStack tankFluid = FluidRegistry.getFluidStack(tags.getString("fluidName"), tags.getInteger("fluidAmount"));
			this.tank.setFluid(tankFluid);
		}
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound tags){
		super.writeToNBT(tags);
		
		tags.setBoolean("hasFluid", tank.getFluid() != null);
		
		String fluidName = "";
		int fluidAmount = 0;
		
		if(tank.getFluid() != null){
			fluidName = tank.getFluid().getFluid().getName();
			fluidAmount = tank.getFluid().amount;
		}
		tags.setString("fluidName", fluidName);
		tags.setInteger("fluidAmount", fluidAmount);
		
		return tags;
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
			worldObj.markChunkDirty(pos, this);
		}
	}

}
