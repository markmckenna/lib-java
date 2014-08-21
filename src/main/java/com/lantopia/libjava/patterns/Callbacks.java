package com.lantopia.libjava.patterns;

import com.google.common.base.Function;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 06/08/2014
 */
public final class Callbacks {
    /**
     * Wrap an appropriately-typed Function as a Callback.
     */
    public <I> Callback<I> from(final Function<I, ?> function) {
        return new Callback<I>() {
            @Override public void execute(final I value) {
                function.apply(value);
            }
        };
    }
}
