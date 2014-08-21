package com.lantopia.libjava.patterns;

import javax.annotation.Nullable;
import javax.inject.Provider;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 23/01/14
 * <p/>
 * Contains a lazily instantiated version of an object of type T.  The object will not be constructed until get()
 * is called on the object.  Once the object is constructed, the Lazy wrapper instance retains it until the Lazy
 * itself is collected; thus it acts as a kind of 'smart reference'.
 */
public class LazyImpl<T> implements Provider<T> {
    private Provider<T> instanceBuilder;

    @Nullable private T instance = null;

    public LazyImpl(final Provider<T> instanceBuilder) {
        this.instanceBuilder = instanceBuilder;
    }

    @Override
    public T get() {
        return (instance == null) ? instance = instanceBuilder.get() : instance;
    }

    @Override
    public String toString() {
        return "LazyImpl{" + instance + '}';
    }
}
