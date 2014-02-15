package com.lantopia.libjava.patterns;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Provider;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 23/01/14
 *
 * Contains a lazily instantiated version of an object of type T.  The object will not be constructed until get()
 * is called on the object.  Once the object is constructed, the Lazy wrapper instance retains it until the Lazy
 * itself is collected; thus it acts as a kind of 'smart reference'.
 */
public class LazyImpl<T> implements Provider<T> {
    @NotNull private Builder<T> instanceBuilder;

    @Nullable private T instance;

    public LazyImpl(@NotNull final Builder<T> instanceBuilder) {
        this.instanceBuilder = instanceBuilder;
    }

    @Override
    public T get() {
        return (instance==null)?instance = instanceBuilder.build():instance;
    }

    @NonNls
    @Override
    public String toString() {
        return "LazyImpl{" + instance + '}';
    }
}
