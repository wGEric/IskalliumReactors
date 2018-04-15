package zairus.iskalliumreactors.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import zairus.iskalliumreactors.IRConfig;
import zairus.iskalliumreactors.block.IRBlocks;
import zairus.iskalliumreactors.guis.BaseContainer;
import zairus.iskalliumreactors.guis.GuiIRController;
import zairus.iskalliumreactors.networking.MessageTile;

public class TileEntityIRController extends TileBase implements ITickable
{
	// @TODO: Make configurable
	public static final int DEFAULT_CAPACITY = 0;
	public static final int MAX_RECEIVE = 10000000;
	public static final int MAX_EXTRACT = 10000000;

	private List<Block> structureBlocks = new ArrayList<Block>();
	private List<Block> casingBlocks = new ArrayList<Block>();
	private List<Block> generatorBlocks = new ArrayList<Block>();

	private IREnergyStorage energyStorage;

	private boolean reactorBuilt = false;
	private int xStart = 0;
	private int yStart = 0;
	private int zStart = 0;
	private int xEnd = 0;
	private int yEnd = 0;
	private int zEnd = 0;
	private int generatorBlockCount = 0;
	private int controlTicks = 0;
	
	public TileEntityIRController()
	{
		energyStorage = new IREnergyStorage(DEFAULT_CAPACITY, MAX_RECEIVE, MAX_EXTRACT, 0);

		structureBlocks.add(IRBlocks.STEEL_CASING);
		structureBlocks.add(IRBlocks.STEEL_CONTROLLER);
		structureBlocks.add(IRBlocks.STEEL_POWERTAP);
		structureBlocks.add(IRBlocks.ISKALLIUM_GLASS);
		
		casingBlocks.add(IRBlocks.STEEL_CASING);
		casingBlocks.add(IRBlocks.STEEL_CONTROLLER);
		casingBlocks.add(IRBlocks.STEEL_POWERTAP);
		
		generatorBlocks.add(IRBlocks.ISKALLIUM);
		generatorBlocks.add(Blocks.AIR);
	}
	
	public int getGeneratorBlockCount()
	{
		return (this.reactorBuilt)? this.generatorBlockCount : 0;
	}

	public int getReactorYield()
	{
		if (!getIsReactor())
			return 0;

		return getGeneratorBlockCount() * IRConfig.eachIskalliumBlockPowerValue;
	}
	
	public boolean getIsReactor()
	{
		return this.reactorBuilt;
	}

	public int extractEnergy(boolean simulate) {
		return extractEnergy(MAX_EXTRACT, simulate);
	}

	public int extractEnergy(int extract, boolean simulate) {
		return energyStorage.extractEnergy(extract, simulate);
	}

	public int getEnergyStored()
	{
		return energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored()
	{
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote) {
			updateStructure();
			updateEnergyStored();
		}
	}

	private void updateStructure() {
		++controlTicks;

		if (controlTicks > 20)
		{
			controlTicks = 0;
			reactorBuilt = checkStructure();
			energyStorage.setMaxEnergyStored(getReactorYield() * 100);
		}
	}

	private void updateEnergyStored() {
	    energyStorage.receiveEnergy(getReactorYield(), false);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("IR_EnergyStored"))
			energyStorage.setEnergyStored(compound.getInteger("IR_EnergyStored"));

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound tag = super.writeToNBT(compound);

		tag.setInteger("IR_EnergyStored", energyStorage.getEnergyStored());

		return tag;
	}

	@SuppressWarnings("unused")
	private void turnGlass()
	{
		BlockPos start = new BlockPos(xStart, yStart, zStart);
		BlockPos end = new BlockPos(xEnd, yEnd, zEnd);
		
		for (int x = start.getX(); x <= end.getX(); ++x)
		{
			for (int z = start.getZ(); z <= end.getZ(); ++z)
			{
				for (int y = start.getY(); y >= end.getY(); --y)
				{
					BlockPos curPos = new BlockPos(x, y, z);
					Block b = this.world.getBlockState(curPos).getBlock();
					
					if (b == IRBlocks.STEEL_POWERTAP)
					{
						TileEntity te = this.world.getTileEntity(curPos);
						if (te instanceof TileEntityIRPowerTap)
						{
							((TileEntityIRPowerTap)te).setController(this);
						}
					}
					
					if (b == Blocks.GLASS || b == Blocks.STAINED_GLASS)
					{
						this.world.setBlockState(curPos, IRBlocks.ISKALLIUM_GLASS.getDefaultState());
					}
				}
			}
		}
	}
	
	private boolean checkStructure()
	{
		boolean isReactor = true;
		boolean hasController = false;
		boolean hasTap = false;
		int tapCount = 0;
		
		boolean checking = true;
		yStart = 0;
		BlockPos pos = this.getPos();
		Block b;
		
		while(checking)
		{
			pos = pos.up();
			b = this.world.getBlockState(pos).getBlock();
			if (this.structureBlocks.contains(b))
				yStart = pos.getY();
			else
				checking = false;
		}
		
		checking = true;
		pos = this.getPos();
		
		while(checking)
		{
			pos = pos.down();
			b = this.world.getBlockState(pos).getBlock();
			if (this.structureBlocks.contains(b))
				yEnd = pos.getY();
			else
				checking = false;
		}
		
		pos = new BlockPos(this.pos.getX(), yStart, this.pos.getZ());
		
		EnumFacing f = null;
		
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			b = this.world.getBlockState(pos.offset(facing)).getBlock();
			if (!structureBlocks.contains(b))
				f = facing;
		}
		
		if (f == null)
			return false;
		
		f = f.getOpposite();
		
		checking = true;
		
		int x1 = pos.getX();
		int z1 = pos.getZ();
		int x2 = 0;
		int z2 = 0;
		int x3 = 0;
		int z3 = 0;
		int x4 = 0;
		int z4 = 0;
		
		while (checking)
		{
			pos = pos.offset(f);
			b = this.world.getBlockState(pos).getBlock();
			if (this.structureBlocks.contains(b))
			{
				x2 = pos.getX();
				z2 = pos.getZ();
			}
			else
			{
				checking = false;
			}
		}
		
		f = f.rotateY();
		pos = new BlockPos(this.pos.getX(), yStart, this.pos.getZ());
		checking = true;
		
		while (checking)
		{
			pos = pos.offset(f);
			b = this.world.getBlockState(pos).getBlock();
			if (this.structureBlocks.contains(b))
			{
				x3 = pos.getX();
				z3 = pos.getZ();
			}
			else
			{
				checking = false;
			}
		}
		
		f = f.getOpposite();
		pos = new BlockPos(this.pos.getX(), yStart, this.pos.getZ());
		checking = true;
		
		while (checking)
		{
			pos = pos.offset(f);
			b = this.world.getBlockState(pos).getBlock();
			if (this.structureBlocks.contains(b))
			{
				x4 = pos.getX();
				z4 = pos.getZ();
			}
			else
			{
				checking = false;
			}
		}
		
		xStart = Math.min(Math.min(x1, x2), Math.min(x3, x4));
		xEnd = Math.max(Math.max(x1, x2), Math.max(x3, x4));
		
		zStart = Math.min(Math.min(z1, z2), Math.min(z3, z4));
		zEnd = Math.max(Math.max(z1, z2), Math.max(z3, z4));
		
		generatorBlockCount = 0;
		
		box_check:
		for (int x = xStart; x <= xEnd; ++x)
		{
			for (int z = zStart; z <= zEnd; ++z)
			{
				for (int y = yStart; y >= yEnd; --y)
				{
					pos = new BlockPos(x, y, z);
					b = this.world.getBlockState(pos).getBlock();
					
					if (b == IRBlocks.STEEL_CONTROLLER)
						hasController = true;
					
					if (b == IRBlocks.STEEL_POWERTAP)
					{
						TileEntity te = this.world.getTileEntity(pos);
						
						if (te instanceof TileEntityIRPowerTap)
							((TileEntityIRPowerTap)te).setController(this);
						
						++tapCount;
						hasTap = true;
					}
					
					if (
							y == yStart 
							|| y == yEnd 
							|| (x == xStart && z == zStart) 
							|| (x == xEnd && z == zEnd) 
							|| (x == xStart && z == zEnd) 
							|| (x == xEnd && z == zStart))
					{
						if (!casingBlocks.contains(b))
						{
							isReactor = false;
							break box_check;
						}
					}
					else if (x == xStart || x == xEnd || z == zStart || z == zEnd)
					{
						if (!structureBlocks.contains(b))
						{
							isReactor = false;
							break box_check;
						}
					}
					else
					{
						if (!generatorBlocks.contains(b))
						{
							isReactor = false;
							break box_check;
						}
						
						if (b == IRBlocks.ISKALLIUM)
							++generatorBlockCount;
					}
				}
			}
		}
		
		if (!hasTap || !hasController || tapCount > 1)
			isReactor = false;
		
		return isReactor;
	}

	@Override
	public Object getGuiClient(InventoryPlayer inventoryPlayer) {
		return new GuiIRController(inventoryPlayer, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventoryPlayer) {
		return new BaseContainer(inventoryPlayer, this);
	}

	@Override
	public MessageTile getGuiMessage() {
		MessageTile message = super.getGuiMessage();

		message.addBoolean(this.reactorBuilt);
		message.addInt(getGeneratorBlockCount());
		message.addInt(getEnergyStored());
		message.addInt(getMaxEnergyStored());

		return message;
	}

	@Override
	public void handleMessageTile(MessageTile message) {
		reactorBuilt = message.getBoolean();
		generatorBlockCount = message.getInt();
	    energyStorage.setEnergyStored(message.getInt());
		energyStorage.setMaxEnergyStored(message.getInt());
	}
}
