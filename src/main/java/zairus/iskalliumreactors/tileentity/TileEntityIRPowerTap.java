package zairus.iskalliumreactors.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityIRPowerTap extends TileBase implements ITickable, IEnergyStorage
{
    private TileEntityIRController controller;
    
	public TileEntityIRPowerTap()
	{
		;
	}
	
	protected void setController(TileEntityIRController c)
	{
		this.controller = c;
	}
	
	@Override
	public void update()
	{
		if (this.world.isRemote || controller == null)
			return;
		
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			TileEntity te = this.world.getTileEntity(this.getPos().offset(facing));

            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
            {
                IEnergyStorage store = te.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
                int maxPossible = controller.extractEnergy(true);
                int energyReceived = store.receiveEnergy(maxPossible, false);
                controller.extractEnergy(energyReceived, false);
            }
		}
	}
	
	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing)
	{
		if (capability != null && capability == CapabilityEnergy.ENERGY)
		{
			return true;
		}
		
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
	{
		if (capability != null && capability == CapabilityEnergy.ENERGY)
		{
			return (T) this;
		}
		
		return super.getCapability(capability, facing);
	}

	@Override
	public int extractEnergy(int extract, boolean simulate)
	{
		return controller.extractEnergy(extract, simulate);
	}
	
	@Override
	public int getEnergyStored()
	{
		return controller.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return controller.getMaxEnergyStored();
	}
	
	@Override
	public int receiveEnergy(int receive, boolean sumulate)
	{
		return 0;
	}
	
	@Override
	public boolean canExtract()
	{
		return true;
	}
	
	@Override
	public boolean canReceive()
	{
		return false;
	}
}
