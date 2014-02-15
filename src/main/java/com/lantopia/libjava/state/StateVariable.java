package com.lantopia.libjava.state;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 23/01/14
 */
public class StateVariable<T extends State> {
    private final AtomicReference<T> state;

    public StateVariable(final T initialState) {
        state = new AtomicReference<>(initialState);
    }

    public final void transition(final T to) throws StateTransitionException {

    }
}
