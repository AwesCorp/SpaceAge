package resonant.lib.grid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import universalelectricity.api.net.IUpdate;

/** A ticker to update all grids. This is multithreaded.
 * 
 * @author Calclavia */
public class UpdateTicker extends Thread
{
    public static final UpdateTicker INSTANCE = new UpdateTicker();

    /** For updaters to be ticked. */
    private final Set<IUpdate> updaters = Collections.newSetFromMap(new WeakHashMap<IUpdate, Boolean>());

    /** For queuing Forge events to be invoked the next tick. */
    private final Queue<Event> queuedEvents = new ConcurrentLinkedQueue<Event>();

    public boolean pause = false;

    /** The time in milliseconds between successive updates. */
    private long deltaTime;

    public UpdateTicker()
    {
        setName("Universal Electricity");
        setPriority(MIN_PRIORITY);
    }

    public static void addNetwork(IUpdate updater)
    {
        synchronized (INSTANCE.updaters)
        {
            INSTANCE.updaters.add(updater);
        }
    }

    public static synchronized void queueEvent(Event event)
    {
        synchronized (INSTANCE.queuedEvents)
        {
            INSTANCE.queuedEvents.add(event);
        }
    }

    public long getDeltaTime()
    {
        return deltaTime;
    }

    public int getUpdaterCount()
    {
        return updaters.size();
    }

    @Override
    public void run()
    {
        try
        {
            long last = System.currentTimeMillis();

            while (true)
            {
                if (!pause)
                {
                    // theProfiler.profilingEnabled = true;
                    long current = System.currentTimeMillis();
                    deltaTime = current - last;

                    /** Tick all updaters. */
                    synchronized (updaters)
                    {
                        Set<IUpdate> removeUpdaters = Collections.newSetFromMap(new WeakHashMap<IUpdate, Boolean>());

                        Iterator<IUpdate> updaterIt = new HashSet<IUpdate>(updaters).iterator();

                        try
                        {
                            while (updaterIt.hasNext())
                            {
                                IUpdate updater = updaterIt.next();

                                if (updater.canUpdate())
                                {
                                    updater.update();
                                }

                                if (!updater.continueUpdate())
                                {
                                    removeUpdaters.add(updater);
                                }
                            }

                            updaters.removeAll(removeUpdaters);
                        }
                        catch (Exception e)
                        {
                            System.out.println("Universal Electricity Threaded Ticker: Failed while tcking updater. This is a bug! Clearing all tickers for self repair.");
                            updaters.clear();
                            e.printStackTrace();
                        }
                    }

                    /** Perform all queued events */
                    synchronized (queuedEvents)
                    {
                        while (!queuedEvents.isEmpty())
                        {
                            MinecraftForge.EVENT_BUS.post(queuedEvents.poll());
                        }
                    }

                    last = current;
                }

                Thread.sleep(50L);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
