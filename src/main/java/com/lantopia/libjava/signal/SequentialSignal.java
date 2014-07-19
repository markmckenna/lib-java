package com.lantopia.libjava.signal;

import com.google.common.base.Function;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 16/07/2014
 *
 * Signal implementation that executes handlers in sequence, in the same thread as the dispatcher.
 */
public class SequentialSignal<T> implements Signal<T> {
    @SuppressWarnings("unchecked")
    private final List<WeakReference<Function<T,Void>>> observers = Collections.synchronizedList(new LinkedList());

    @Override
    public void notify(final Function<T, Void> h) {
        final WeakReference<Function<T, Void>> ref = new WeakReference<>(h);
        observers.add(ref);
    }

    @Override
    public void raise(final T value) {
        final ListIterator<WeakReference<Function<T, Void>>> iterator = observers.listIterator();
        while (iterator.hasNext()) {
            final WeakReference<Function<T, Void>> ref = iterator.next();
            final Function<T, Void> fun = ref.get();
            if (fun == null) iterator.remove();
            else fun.apply(value);
        }
    }
}
