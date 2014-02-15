package com.lantopia.libjava.util;

import java.util.*;

/**
 * @author Mark McKenna %lt;mark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 14/02/14
 */
@SuppressWarnings("UnusedDeclaration")
public class LibJava {
    private LibJava() {}

    /**
     * Builds a set using a fluent interface.
     *
     * Example: LibJava.makeSetOf(Integer.class).mutable(false).synchronized(true).with(nums).build();
     *
     * @param clazz Class of the elements the set takes
     * @param <T> Type of elements the set takes
     *
     * @return The set, built according to a sensible set of rules based on the given inputs
     */
    public static <T> SetBuilder<T> makeSetOf(final Class<T> clazz) { return new SetBuilderImpl<>(); }

    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object o) { return (T)o; }


    private static class SetBuilderImpl<T> implements SetBuilder<T> {
        private boolean mutable = true;
        private boolean sync = false;

        private Object elems = null;

        public SetBuilder<T> sync(final boolean b) { this.sync = b; return this; }
        public SetBuilder<T> mutable(final boolean b) { this.mutable = b; return this; }
        public SetBuilder<T> with(final Collection<T> elements) { this.elems = elements; return this; }

        @SafeVarargs
        public final SetBuilder<T> with(final T... elements) { this.elems = elements; return this; }

        @SuppressWarnings("unchecked")
        public Set<T> build() {
            final Collection<T> e = (elems==null)
                    ? Collections.EMPTY_SET
                    : (elems instanceof Collection) ? (Collection<T>)elems : Arrays.asList((T[]) elems);

            final Set<T> out = (e.size() > 20) ? new HashSet<>(e) : new TreeSet<>(e);

            if (!mutable) return Collections.unmodifiableSet(out);
            if (sync) return Collections.synchronizedSet(out);
            return out;
        }

        public Set<T> get() { return build(); }
    }
}
