package com.lantopia.libjava.patterns;

import javax.inject.Provider;

/**
 * @author Mark McKenna %lt;mark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 14/02/14
 * <p/>
 * Represents a Provider instance that lazily creates the type that it provides.
 */
@SuppressWarnings("UnusedDeclaration")
public interface Lazy<T> extends Provider<T> {
}
