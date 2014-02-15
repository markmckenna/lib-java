package com.lantopia.libjava.patterns;

import javax.inject.Provider;

/**
 * @author Mark McKenna %lt;mark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 14/02/14
 *
 * Represents a Provider that assembles its delegate type and provides it on demand.  Might be either lazy or
 * eager, and may require a variety of parameters.
 */
public interface Builder<T> extends Provider<T> {
    /**
     * @return The same thing that {@link javax.inject.Provider#get()} returns.
     */
    T build();
}
