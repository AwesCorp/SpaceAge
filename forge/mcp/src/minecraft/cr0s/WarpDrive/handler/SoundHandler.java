package cr0s.WarpDrive.handler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundHandler
{
    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void onSoundLoad(SoundLoadEvent event)
    {
        try
        {
            System.out.println("[WarpDrive] Registering sound files...");
            event.manager.addSound("warpdrive:warp.ogg");
            event.manager.addSound("warpdrive:hilaser.ogg");
            event.manager.addSound("warpdrive:midlaser.ogg");
            event.manager.addSound("warpdrive:lowlaser.ogg");
            event.manager.addSound("warpdrive:cl.ogg");
            event.manager.addSound("warpdrive:dcl.ogg");
        }
        catch (Exception e)
        {
            System.err.println("Failed to register sound: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}