package com.lantopia.libjava.util;

import com.google.common.base.Optional;

import javax.annotation.Nullable;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 07/08/2014
 */
public final class BuilderTools {
    private BuilderTools() {}

    /**
     * @return value if not null; otherwise throw {@link java.lang.IllegalStateException}
     */
    public static <T> T require(@Nullable final T value, final String name) {
        if (value != null) return value;
        throw new IllegalStateException(String.format("'%s' is required", name));
    }

    /**
     * @return value if present; otherwise throw {@link java.lang.IllegalStateException}
     */
    public static <T> T require(final Optional<T> value, final String name) {
        if (value.isPresent()) return value.get();
        throw new IllegalStateException(String.format("'%s' is required", name));
    }
}
