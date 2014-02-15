package com.lantopia.libjava.functional;

import java.util.concurrent.Callable;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 26/01/14
 *
 * @param <I> The input type of the functor
 * @param <O> The output type of the functor
 * @param <X> The exception type of the functor; if no exceptions are expected, provide RuntimeException
 *
 * A 'functor' that takes a single valued input and returns a single valued output.
 */
@SuppressWarnings({"UnusedDeclaration", "UncheckedExceptionClass"})
public interface Function<I,O, X extends Throwable> {
    O apply(I input) throws X;

    /**
     * A builder of functors.  Provides various facilities in support of gluing other function-like systems
     * into this one.
     */
    @SuppressWarnings("PublicInnerClass")
    abstract class Builder<I,O,X extends Throwable> implements javax.inject.Provider<Function> {
        private Builder() { }

        /**
         * Wraps the given {@link java.util.concurrent.Callable} with a Function object.  The resultant Function will
         * be a NullaryFunction, which remains type-compatible with Callable.
         *
         * @param callable The callable that will be executed when the returned Function is executed
         * @return A Builder whose get() method returns a Function wrapping the Callable.
         */
        static <O,X extends Exception> Builder<Void,O,X> with(final Callable<O> callable) {
            return new Builder<Void,O,X>() {
                @Override
                public NullaryFunction<O,X> get() {
                    return new NullaryFunction<O,X>() {
                        @SuppressWarnings({"unchecked", "OverlyBroadCatchBlock", "ConstantConditions"})
                        @Override
                        public O apply() throws X {
                            try {
                                return callable.call();
                            } catch(final Throwable t) {
                                throw (X)t;
                            }
                        }
                    };
                }
            };
        }
    }
}
