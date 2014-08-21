package com.lantopia.libjava.test;

import com.lantopia.libjava.patterns.Callback;

import javax.annotation.Nullable;

/**
 * General purpose testing callback wrapper that supports throwing general exceptions within the callback body.
 */
@SuppressWarnings({"ProhibitedExceptionThrown", "AbstractClassNeverImplemented", "ProhibitedExceptionDeclared"})
public abstract class TestCallback<T> implements Callback<T> {
    @Override public final void execute(@Nullable final T value) {
        try {
            verify(value);
        } catch (final Exception e) {
            throw new RuntimeException("Callback verification excepted", e);
        }
    }

    protected abstract void verify(final T value) throws Exception;
}
