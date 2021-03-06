package com.lantopia.libjava.util;

import javax.inject.Provider;
import java.util.Collection;
import java.util.Set;

/**
 * @author Mark McKenna %lt;mark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 14/02/14
 */
@SuppressWarnings({"unchecked", "UnusedDeclaration"})
public interface SetBuilder<T> extends Provider<Set<T>> {
    SetBuilder<T> sync(final boolean b);

    SetBuilder<T> mutable(final boolean b);

    SetBuilder<T> with(final Collection<T> elements);

    SetBuilder<T> with(final T... elements);
}
