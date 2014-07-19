package com.lantopia.libjava.signal;

import com.google.common.base.Function;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 16/07/2014
 */
public interface Signal<T> {
    void notify(Function<T, Void> h);
    void raise(T value);
}
