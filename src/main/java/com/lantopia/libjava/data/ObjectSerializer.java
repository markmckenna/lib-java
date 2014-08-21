package com.lantopia.libjava.data;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 19/07/2014
 * <p/>
 * Object serialization interface.  Implementations of this are able to transform an object between an in-memory
 * form and a serial (string) form.
 */
public interface ObjectSerializer<T> {
    T parse(final String jsonData);

    String serialize(final T jsonObject);
}
