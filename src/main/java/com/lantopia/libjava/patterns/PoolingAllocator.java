package com.lantopia.libjava.patterns;

import javax.inject.Provider;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 28/07/2014
 * <p/>
 * A memory allocator that collects soft and hard references to objects in order to ensure that a low-water-mark
 * of memory objects are retained and reused.  This helps avoid 'memory churn' in real-time environments where
 * memory churn might contribute negatively to system performance.
 */
@SuppressWarnings("unchecked")
public class PoolingAllocator implements Allocator {
    private final ReferenceQueue softQueue = new ReferenceQueue<>();
    private final Map<Class, LinkedBlockingQueue> hardInstances = new HashMap<>();
    private final Map<Class, LinkedBlockingQueue<SoftReference>> softInstances = new HashMap<>();
    private final Map<Reference, Class> instanceTypes = new HashMap<>();
    private int capacity;


    public PoolingAllocator(final int perTypeCapacity) {
        this.capacity = perTypeCapacity;
    }

    protected LinkedBlockingQueue getPool(final Map map, final Class type) {
        // May be overridden by child classes to supply synchronization
        if (!map.containsKey(type))
            map.put(type, new LinkedBlockingQueue(capacity));
        return (LinkedBlockingQueue) map.get(type);
    }

    @Override public <T> T allocate(final Class<T> type, final Provider<T> provider) {
        final LinkedBlockingQueue instances = getPool(hardInstances, type);

        if (!instances.isEmpty()) {
            try {
                return (T) instances.take();
            } catch (final InterruptedException e) {
                throw new AllocatorException("Allocator thread interrupted!", e);
            }
        }

        if (!softInstances.containsKey(type))
            softInstances.put(type, new LinkedBlockingQueue<SoftReference>());

        final LinkedBlockingQueue<SoftReference> sInstances = getPool(softInstances, type);

        if (!instances.isEmpty()) {
            try {
                final SoftReference reference = sInstances.take();
                if (!reference.isEnqueued()) return (T) reference.get();
            } catch (final InterruptedException e) {
                throw new AllocatorException("Allocator thread interrupted!", e);
            }
        }

        final T instance = provider.get();
        instances.offer(instance); // If it is rejected we don't care
        final SoftReference softReference = new SoftReference(instance, softQueue);
        sInstances.offer(softReference);
        instanceTypes.put(softReference, type);

        return instance;
    }

    /**
     * Clears dead soft references from the various object pools.
     */
    public void maintain() {
        while (true) {
            final Reference ref = softQueue.poll();
            if (ref == null) return;
            final Class type = instanceTypes.remove(ref);
            getPool(softInstances, type).remove(ref);
        }
    }
}
