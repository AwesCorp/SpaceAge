package cr0s.WarpDrive.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cr0s.WarpDrive.WarpDrive;
import cr0s.WarpDrive.tile.TileEntityReactor;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockReactor extends Block
{
    private Icon[] iconBuffer;

    private final int ICON_INACTIVE_SIDE = 0, ICON_BOTTOM = 1, ICON_TOP = 2, ICON_SIDE_ACTIVATED = 3;

    public BlockReactor(int id, int texture, Material material)
    {
        super(id, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        iconBuffer = new Icon[4];
        iconBuffer[ICON_INACTIVE_SIDE] = par1IconRegister.registerIcon("warpdrive:coreSideInactive");
        iconBuffer[ICON_BOTTOM] = par1IconRegister.registerIcon("warpdrive:coreBottom");
        iconBuffer[ICON_TOP] = par1IconRegister.registerIcon("warpdrive:coreTop");
        iconBuffer[ICON_SIDE_ACTIVATED] = par1IconRegister.registerIcon("warpdrive:coreSideActive");
    }

    @Override
    public Icon getIcon(int side, int metadata) {
    	switch(side) {
    		case 0:
    			return iconBuffer[ICON_BOTTOM];
    		case 1:
    			return iconBuffer[ICON_TOP];
    		case 2:
    			switch(metadata) {
    				case 0: 
    					return iconBuffer[ICON_INACTIVE_SIDE];
    				case 1:
    					return iconBuffer[ICON_SIDE_ACTIVATED];
    			}
    		case 3:
    			switch(metadata) {
    				case 0: 
    					return iconBuffer[ICON_INACTIVE_SIDE];
    				case 1:
    					return iconBuffer[ICON_SIDE_ACTIVATED];
    			}
    		case 4:
    			switch(metadata) {
    				case 0: 
    					return iconBuffer[ICON_INACTIVE_SIDE];
    				case 1:
    					return iconBuffer[ICON_SIDE_ACTIVATED];
    			}
    		case 5:
    			switch(metadata) {
    				case 0: 
    					return iconBuffer[ICON_INACTIVE_SIDE];
    				case 1:
    					return iconBuffer[ICON_SIDE_ACTIVATED];
    			}
    	}
    	return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityReactor();
    }
    
    @Override
    public boolean hasTileEntity(int metadata) {
    	return true;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return this.blockID;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return false;
        }

        TileEntityReactor reactor = (TileEntityReactor)par1World.getBlockTileEntity(par2, par3, par4);

        //if (reactor != null) {
        System.out.println("CORE: JUST ABOUT TO OPEN GUI");
            par5EntityPlayer.openGui(WarpDrive.instance, 1, par1World, par2, par3, par4);
            System.out.println("CORE: JUST OPENED GUI");
        //}
        return true;
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        if (te != null && te instanceof TileEntityReactor) {
            WarpDrive.instance.registry.removeFromRegistry((TileEntityReactor)te);
            te.invalidate();
        }

        WarpDrive.instance.registry.removeDeadCores();
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
}