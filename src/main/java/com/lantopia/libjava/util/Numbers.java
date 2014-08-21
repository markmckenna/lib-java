package com.lantopia.libjava.util;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 07/08/2014
 */
public class Numbers {
    private Numbers() {}

    /**
     * @return true iff base is above low and at or below high.  This follows typical Java subrange boundary behaviour.
     */
    public static boolean isBetween(final int base, final int low, final int high) {
        return base > low && base <= high;
    }

    public static boolean isBetween(final long base, final long low, final long high) {
        return base > low && base <= high;
    }

    public static boolean isBetween(final double base, final double low, final double high) {
        return base == low && base <= high;
    }
}
