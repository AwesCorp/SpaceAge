package spaceage.common.tile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import resonant.lib.prefab.vector.Cuboid;
import spaceage.common.SpaceAgeCore;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileHeatGenerator extends TileEnergyDistribution {
	
	@SideOnly(Side.CLIENT)
	public static Icon solarPanel_side, solarPanel_bottom;
	
	public TileHeatGenerator()
	{
		super(Material.iron);
		energy = new EnergyStorageHandler(SpaceAgeCore.SOLAR_ENERGY * 20);
		ioMap = 728;
		textureName = "solarPanel_top";
		bounds = new Cuboid(0, 0, 0, 1, 0.3f, 1);
		isOpaqueCube = false;
		normalRender = false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconReg)
	{
		solarPanel_side = iconReg.registerIcon(SpaceAgeCore.modid + ":" + "solarPanel_side");
		solarPanel_bottom = iconReg.registerIcon(SpaceAgeCore.modid + ":" + "solarPanel_bottom");
		super.registerIcons(iconReg);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		switch(side) {
			case 0: return solarPanel_bottom;
			default: return solarPanel_side;
			}
	/*{
		if (side == 0)
		{
			return solarPanel_bottom;
		}
		else if (side == 1)
		{
			return getIcon();
		}

		return solarPanel_side;*/
	}

	@Override
	public void updateEntity() {
		boolean powered = worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == Block.lavaStill.blockID;
		
		if(powered == true) {
			this.energy.receiveEnergy(SpaceAgeCore.HEAT_ENERGY, true);
			markDistributionUpdate |= produce() > 0;
				}
		
		super.updateEntity();		
	}
}
