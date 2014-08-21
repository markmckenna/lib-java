package com.lantopia.libjava.patterns;

import javax.inject.Provider;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 28/07/2014
 */
public interface Allocator {
    /**
     * Allocates an object of the given type, as provided by the given provider.  The provider could be a
     * builder pattern or some such and should be considered reusable.
     */
    <T> T allocate(Class<T> type, Provider<T> provider);
}
