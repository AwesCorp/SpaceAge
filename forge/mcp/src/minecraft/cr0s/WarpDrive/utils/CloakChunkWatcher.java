package cr0s.WarpDrive.utils;

import java.util.ArrayList;
import java.util.List;

import cr0s.WarpDrive.WarpDrive;
import cr0s.WarpDrive.manager.CloakManager.CloakedArea;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet56MapChunks;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkWatchEvent;

public class CloakChunkWatcher {
	@ForgeSubscribe
	public void chunkLoaded(ChunkWatchEvent event)
	{
		EntityPlayerMP p = event.player;
		ChunkCoordIntPair chunk = event.chunk;
		
		// Check chunk for locating in cloaked areas
		ArrayList<CloakedArea> cloaks = WarpDrive.instance.cloaks.getCloaksForPoint(p.worldObj.provider.dimensionId, chunk.getCenterXPos(), 0, chunk.getCenterZPosition(), true);
		if (cloaks.size() == 0)
			return;
		
		//Chunk c = p.worldObj.getChunkFromChunkCoords(chunk.chunkXPos, chunk.chunkZPos);
		for (CloakedArea area : cloaks) {
			area.sendCloakPacketToPlayer(p, false);
		}
		
		/*List<Chunk> list = new ArrayList<Chunk>();
		list.add(c);
		
		// Send obscured chunk
		System.out.println("[Cloak] Sending to player " + p.username + " obscured chunk at (" + chunk.chunkXPos + "; " + chunk.chunkZPos + ")");
		((EntityPlayerMP)p).playerNetServerHandler.sendPacketToPlayer(new Packet56MapChunks(list));*/
		
	}
}
