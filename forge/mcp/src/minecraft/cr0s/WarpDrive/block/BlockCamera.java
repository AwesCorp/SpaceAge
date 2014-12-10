package cr0s.WarpDrive.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cr0s.WarpDrive.tile.TileEntityCamera;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockCamera extends Block
{
    private Icon[] iconBuffer;

    private final int ICON_SIDE = 0;

    public BlockCamera(int id, int texture, Material material)
    {
        super(id, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        iconBuffer = new Icon[1];
        // Solid textures
        iconBuffer[ICON_SIDE] = par1IconRegister.registerIcon("warpdrive:cameraSide");
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        return iconBuffer[ICON_SIDE];
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityCamera();
    }
    
    @Override
    public boolean hasTileEntity(int meta) {
    	return true;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID;
    }
}