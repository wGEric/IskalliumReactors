package zairus.iskalliumreactors.tileentity;

import net.minecraftforge.energy.EnergyStorage;

public class IREnergyStorage extends EnergyStorage {
    public IREnergyStorage(int capacity) {
        super(capacity);
    }

    public IREnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public IREnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public IREnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    public void setMaxEnergyStored(int capacity) {
        this.capacity = capacity;
    }
}
