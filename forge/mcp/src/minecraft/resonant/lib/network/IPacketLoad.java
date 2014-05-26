package resonant.lib.network;

import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

/** Used for object that only have one set of data to send and receive. Mainly designed to encode
 * data from simple objects
 * 
 * @author DarkGuardsman */
public interface IPacketLoad
{
    public void readPacket(ByteArrayDataInput data);

    public void loadPacket(DataOutputStream data) throws IOException;
}
