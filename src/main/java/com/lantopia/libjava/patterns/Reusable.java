package com.lantopia.libjava.patterns;

import java.io.Closeable;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 28/07/2014
 * <p/>
 * Represents objects that can be used more than once.  These
 * objects' {@link #close()} method should be build so as to
 * release temporary resources associated with the object's
 * previous life, and leave it in a state from which it could be
 * reinitialized for use again.  They should generally also
 * expose some subsidiary initialization method that can be
 * used to set them back up for future use.
 * <p/>
 * This interface exposes no methods--it just marks classes that
 * can be reused (such as through a Pool or similar).
 */
public interface Reusable extends Closeable {}
