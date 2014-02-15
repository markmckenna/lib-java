package com.lantopia.libjava.functional;

import java.util.concurrent.Callable;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 26/01/14
 *
 * Abstract implementation that unifies the Callable and Function interface trees.
 */
@SuppressWarnings({"UnusedDeclaration", "PublicMethodNotExposedInInterface"})
public abstract class NullaryFunction<O,X extends Exception> implements Function<Void,O,X>, Callable<O> {
    public abstract O apply() throws X;

    @Override public final O apply(final Void input) throws X { return apply(); }
    @Override public final O call() throws X { return apply(); }
}
