package uedevkit.tile;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.vector.Vector3;

public abstract class TileElectricInventoryNetworked extends TileElectricNetworked implements ISidedInventory {
	
	public ItemStack[] inventory;
	public String invName = "";
	
	/**
	 * Recharges electric item.
	 */
	public void recharge(ItemStack itemStack) {
		this.energy.extractEnergy(CompatibilityModule.chargeItem(itemStack, this.energy.getEnergy(), true), true);
	}

	/**
	 * Discharges electric item.
	 */
	public void discharge(ItemStack itemStack) {
		this.energy.receiveEnergy(CompatibilityModule.dischargeItem(itemStack, this.energy.getEmptySpace(), true), true);
	}
	
	public boolean isBatteryFull(ItemStack itemStack) {
		if(itemStack != null && (CompatibilityModule.isHandler(itemStack.getItem()))) {
			if(CompatibilityModule.getEnergyItem(itemStack) >= CompatibilityModule.getMaxEnergyItem(itemStack)) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean isBatteryEmpty(ItemStack itemStack) {
		if(itemStack != null && (CompatibilityModule.isHandler(itemStack.getItem()))) {
			if(CompatibilityModule.getEnergyItem(itemStack) <= 0) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	 /**
     * Used for temporary constructors. Not recommended to be used. 
     * @author SkylordJoel
     */
    public TileElectricInventoryNetworked() {
        super();
    }

    /**
     * Used for blocks that can only hold energy, and possibly output to an item, but can adjust their transfer rate. Recommended. 
     * @author SkylordJoel
     */
    public TileElectricInventoryNetworked(long energyCapacity, long transferRate) {
        super(energyCapacity, transferRate);
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
    }
}