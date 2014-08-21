package com.lantopia.libjava.patterns;


/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 03/08/2014
 */
public interface Callback<I> {
    public abstract void execute(final I value);
}
