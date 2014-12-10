package spaceage.common.block;

import java.util.List;
import java.util.Random;

import spaceage.common.SpaceAgeCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockVulcan extends Block {

	public BlockVulcan(int id, Material material) {
		super(id, material);
		this.setCreativeTab(SpaceAgeCore.tabSA);
		this.setLightValue(0.2F);
	}
	
	private Type type;
	
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[5];
		
		for(int i = 0; i < icons.length; i++) {
			icons[i] = par1IconRegister.registerIcon(SpaceAgeCore.modid + ":vulcan/" + (this.getUnlocalizedName().substring(5)) + i);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int metadata) {
		switch(metadata) {
		case 0:
			return icons[3];
		case 1:
			switch(side) {
				case 0: 
					return icons[1];
				case 1:
					return icons[1];
				default: 
					return icons[2];
			}
		case 2: 
			return icons[0];
		case 3:
			return icons[4];
		default: {
			System.out.println("Invalid metadata for " + this.getUnlocalizedName());
			return icons[0];
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 4; i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}

		
	}
	
	public int damageDropped(int meta) {
		switch(type.ordinal()) {
			case 2:
				return 0;
			case 3: 
				return 14;
			default:
				return meta;
		}
	}
	
	public int idDropped(int par1, Random par2Random, int par3) {
		switch(type.ordinal()) {
			case 2: 
				return Item.glowstone.itemID;
			case 3:
				return SpaceAgeCore.meta.itemID;
			default:
				return this.blockID;
		}
	}
	
	@Override
	public int quantityDropped(Random random) {
		switch(type.ordinal()) {
			case 2:
				return 2 + random.nextInt(3);
			case 3:
				return 2 + random.nextInt(3);
			default:
				return 1;
		}
	}

	public static enum Type {
		ASH, QUARTZ_WOOD, GLOW_LEAVES, FIRE_ESSCENCE;
	}
}