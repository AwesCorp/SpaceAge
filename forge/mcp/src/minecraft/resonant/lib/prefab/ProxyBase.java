package resonant.lib.prefab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class ProxyBase implements IGuiHandler
{
    public void preInit()
    {
    }

    public void init()
    {
    }

    public void postInit()
    {
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    public boolean isPaused()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
