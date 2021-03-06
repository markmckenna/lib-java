package com.lantopia.libjava.util;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * @author Mark McKenna %ltmark.denis.mckenna@gmail.com>
 * @version 0.1
 * @since 23/01/14
 * <p/>
 * Defines a collection of reasonable sentinel objects, good for safe development with collections objects and
 * other scenarios.
 * <p/>
 * These values are suitable for use as sentinel values that can be == compared with a static constant to determine
 * whether the variable is in a special state. The returned instance is always immutable and usually has no normal
 * contents; but comparison with other instances, even obtained from these methods, will be false.
 * <p/>
 * Using these for sentinel values is better than (for example) Collections.empty*() because of its uniqueness
 * properties. Several such sentinels can be distinctly created for a single variable; it will be distinct from simply
 * having a real empty Collection instance attached; and sentinels created for other Collection variables will not
 * match this one, providing an additional layer of protection.
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "override", "SerializableHasSerializationMethods",
        "FinalClass", "DesignForExtension", "UtilityClass", "StaticMethodOnlyUsedInOneClass"})
public final class Markers {
    private Markers() { }

    /**
     * @param <T> The subtype of the Iterator instance
     * @return a unique, immutable Iterator iterating over no items.
     */
    public static <T> Iterator<T> markerIterator() { return new MarkerIterator<>(); }

    public static <T> Iterator<T> markerIterator(final String name) { return new MarkerIterator<>(name); }

    /**
     * @param <T> The content type of the returned set
     * @return An immutable, empty, unique Set instance, suitable for use as a marker instance
     */
    public static <T> Set<T> markerSet() { return new MarkerSet<>(); }

    public static <T> Set<T> markerSet(final String name) { return new MarkerSet<>(name); }

    /**
     * @param <K> Key type of the Map
     * @param <V> Value type of the Map
     * @return A unique, empty, immutable Map instance, suitable for use as a marker instance.
     */
    public static <K, V> Map<K, V> markerMap() { return new MarkerMap<>(); }

    public static <K, V> Map<K, V> markerMap(final String name) { return new MarkerMap<>(name); }

    /**
     * @return A unique object with no special getProperties.
     */
    public static Object marker() { return new Object(); }

    /**
     * NOTE: Two calls to this method with the same name will produce two distinct marker objects.
     *
     * @param name The name for this marker
     * @return A unique marker object with the given name.
     */
    public static Object marker(final String name) {
        return new Object() {
            public String toString() { return name; }
        };
    }


    private static final class MarkerIterator<T> implements Iterator<T>, Serializable {
        private static final long serialVersionUID = -3070237580267848407L;

        private final String name;

        MarkerIterator() { name = ""; }

        MarkerIterator(final String name) { this.name = name; }

        public boolean hasNext() { return false; }

        public T next() { throw new NoSuchElementException("Marker Iterator references no elements"); }

        public void remove() { throw new IllegalStateException("Marker Iterator is immutable"); }

        public String toString() {
            return "MarkerIterator{" + "name='" + name + '\'' + '}';
        }
    }


    private static class MarkerCollection<T> implements Collection<T>, Serializable {
        private static final long serialVersionUID = -8003381120236505478L;
        private static final Object[] EmptyArray = new Object[0];

        protected final String name;

        MarkerCollection() { name = ""; }

        MarkerCollection(final String name) { this.name = name; }

        protected static UnsupportedOperationException newUnsupported() {
            throw new UnsupportedOperationException("Marker Collection is always empty and immutable");
        }

        public int size() { return 0; }

        public boolean isEmpty() { return true; }

        public boolean contains(final Object o) { return false; }

        @Nonnull public Iterator<T> iterator() { return Collections.emptyIterator(); }

        @Nonnull public Object[] toArray() { return EmptyArray; }

        @Nonnull @SuppressWarnings("AssignmentToNull")
        public <U> U[] toArray(final U[] a) {
            if (a.length > 0) a[0] = null;
            return a;
        }

        public boolean add(final T e) { throw newUnsupported(); }

        public boolean remove(final Object o) { return false; }

        public boolean containsAll(final Collection<?> c) { return false; }

        public boolean addAll(final Collection<? extends T> c) { throw newUnsupported(); }

        public boolean removeAll(final Collection<?> c) { return false; }

        public boolean retainAll(final Collection<?> c) { return false; }

        public void clear() { }

        public boolean equals(final Object obj) { return this == obj; }

        public int hashCode() { return 0; }

        public String toString() {
            return "MarkerCollection{" + "name='" + name + '\'' + '}';
        }
    }


    private static final class MarkerSet<T> extends MarkerCollection<T> implements Set<T> {
        private static final long serialVersionUID = -5076792551969309398L;

        MarkerSet() { }

        MarkerSet(final String name) { super(name); }

        public String toString() {
            return "MarkerSet{" + "name='" + name + '\'' + '}';
        }
    }


    private static class MarkerList<T> extends MarkerCollection<T> implements List<T> {
        private static final long serialVersionUID = -1411499141905873861L;

        MarkerList() { }

        MarkerList(final String name) { super(name); }

        protected static IndexOutOfBoundsException newOutOfBounds() {
            throw new IndexOutOfBoundsException("Marker List is always empty");
        }

        public boolean addAll(final int index, final Collection<? extends T> c) { throw newUnsupported(); }

        public T get(final int index) { throw newOutOfBounds(); }

        public T set(final int index, final T element) { throw newUnsupported(); }

        public void add(final int index, final T element) { throw newUnsupported(); }

        public T remove(final int index) { throw newUnsupported(); }

        public int indexOf(final Object o) { return -1; }

        public int lastIndexOf(final Object o) { return -1; }

        @Nonnull public ListIterator<T> listIterator() { return Collections.emptyListIterator(); }

        @Nonnull public ListIterator<T> listIterator(final int index) { return Collections.emptyListIterator(); }

        @Nonnull public List<T> subList(final int fromIndex, final int toIndex) {
            if ((fromIndex < 0) || (toIndex < fromIndex) || (toIndex > 0)) throw newOutOfBounds();
            return this;
        }

        public String toString() {
            return "MarkerList{" + "name='" + name + '\'' + '}';
        }
    }


    private static class MarkerMap<K, V> implements Map<K, V>, Serializable {
        private static final long serialVersionUID = -380903779251544412L;

        private final Optional<String> name;

        MarkerMap() { name = Optional.absent(); }

        MarkerMap(final String name) { this.name = Optional.of(name); }

        protected static UnsupportedOperationException newUnsupported() {
            throw new UnsupportedOperationException("Marker Map is always empty and immutable");
        }

        public int size() {return 0;}

        public boolean isEmpty() {return true;}

        public boolean containsKey(final Object key) {return false;}

        public boolean containsValue(final Object value) {return false;}

        @Nullable public V get(final Object key) {return null;}

        public V put(final K key, final V value) { throw newUnsupported(); }

        public V remove(final Object key) { throw newUnsupported(); }

        public void putAll(final Map<? extends K, ? extends V> m) { throw newUnsupported(); }

        public void clear() { }

        @Nonnull public Set<K> keySet() {return Collections.emptySet();}

        @Nonnull public Collection<V> values() {return Collections.emptySet();}

        @Nonnull public Set<Entry<K, V>> entrySet() {return Collections.emptySet();}

        public boolean equals(final Object obj) { return this == obj; }

        public int hashCode() { return 0; }

        public String toString() {
            return "MarkerMap{" + "name='" + name + '\'' + '}';
        }
    }
}
