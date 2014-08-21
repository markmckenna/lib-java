package com.lantopia.libjava.state;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 01/02/14
 */
public final class States {
    private States() { }

    public static State.Builder stateBuilder() { return new State.Builder(); }
}
