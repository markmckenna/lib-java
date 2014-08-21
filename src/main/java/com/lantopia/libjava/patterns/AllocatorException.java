package com.lantopia.libjava.patterns;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 28/07/2014
 */
class AllocatorException extends RuntimeException {
    public AllocatorException(final String s, final InterruptedException e) {super(s, e);}
}
