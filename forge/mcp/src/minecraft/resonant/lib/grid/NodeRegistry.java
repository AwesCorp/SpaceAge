package resonant.lib.grid;

import java.util.HashMap;

import resonant.api.grid.INode;
import resonant.api.grid.INodeProvider;

/** A dynamic node loader for registering different nodes for different node interfaces.
 * 
 * @author Calclavia */
public class NodeRegistry
{
    private static final HashMap<Class, Class> INTERFACE_NODE_MAP = new HashMap<Class, Class>();

    public static void register(Class nodeInterface, Class nodeClass)
    {
        INTERFACE_NODE_MAP.put(nodeInterface, nodeClass);
    }

    public static <N extends INode> N get(INodeProvider parent, Class<N> nodeInterface)
    {
        Class nodeClass = INTERFACE_NODE_MAP.get(nodeInterface);

        try
        {
            return (N) nodeClass.getConstructor(INodeProvider.class).newInstance(parent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}