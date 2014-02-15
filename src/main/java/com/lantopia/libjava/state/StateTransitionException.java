package com.lantopia.libjava.state;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 23/01/14
 */
public class StateTransitionException extends RuntimeException {
    public StateTransitionException(final State from, final State to) {
        super(String.format("Cannot transition from state %s into state %s", from.getName(), to.getName()));
    }
}
