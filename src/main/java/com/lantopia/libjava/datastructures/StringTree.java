package com.lantopia.libjava.datastructures;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 09/06/2014
 *
 * Represents a tree-based structure to efficiently store values keyed by {@link CharSequence}.  Each interior node
 * represents a string with length equal to or greater than its depth in the tree, and zero or more child nodes
 * representing all stored strings that have it as a prefix.  Subtrees with only one node chain will be
 * abbreviated by simply storing the entry at an 'aggregate leaf' whose internal value is the same as the node that
 * would have been present at the true leaf, but eliminating all of the interior nodes leading to it (which would have
 * formed a chain--not very efficient leaf storage).
 *
 * Each interior node starts with an initial capacity of children, which grows as collisions are detected.  Each child
 * string goes into a bucket keyed by the next character in the sequence, modulo the number of buckets.  When
 * collisions happen, the number of buckets is roughly doubled.  To minimize harmonic interference, the number of
 * buckets is set to the next prime number beyond twice as many buckets as currently stored.
 *
 * TODO: Implement single-value subtree pruning
 */
@SuppressWarnings("UnusedDeclaration")
class StringTree<T> {
    StringTree() {
        root = new Bucket<>('\0'); // The root bucket is special; doesn't represent a value
    }

    // Returns the bucket that stores the given key, or null if no bucket.
    Bucket<T> getBucket(final char[] key) {
        final SearchResult<T> result = findLeaf(key);
        return result.found ? result.bucket : null;
    }



    private static class SearchResult<T> {
        final int depth;
        final Bucket<T> bucket;

        SearchResult(final int depth, final Bucket<T> bucket) {
            this.depth = depth;
            this.bucket = bucket;
        }
    }

    // Locate the leaf node that either represents the given key, or would be the parent of the key if
    // it were present in the tree.
    // * If input is "", returns (0, root), which represents the empty string and is always in the tree (TODO: think)
    // * If input is "a" and tree is {}, return (0, root)
    // * If input is "aa" and tree is {"b"}, return (0, root)
    // * If input is "aa" and tree is {"a"}, return (1, node("a"))
    // * If input is "aa" and tree is {"aa"}, return (2, node("aa"))
    // * If input is "aa" and tree is {"aaa"}, (2, node(null))
    // If the returned depth value matches the length of the input, then the input was found.  If the returned depth
    // value is less than the length of the input, then it was not found, and the returned node is an appropriate place
    // to add a parent.
    private SearchResult<T> findLeaf(final char[] key) {
        final int len = key.length;

        Bucket<T> cur = root, prior = null;
        int pos = 0;

        while (cur != null && pos < len) {
            prior = cur;
            cur = cur.getChild(key[pos++]);
        }

        return (cur==null)
                ? new SearchResult<>(pos-1, prior)
                : new SearchResult<>(pos, cur);
    }

    // Returns the bucket that stores the given key, adding it first if necessary.
    private Bucket<T> getOrAddBucket(final char[] key) {
        final int len = key.length;

        if (root==null) root = new Bucket<>(key[0]);

        Bucket<T> cur = root;
        int pos = 0;

        while (pos < len)
            cur = cur.getOrAddChild(key[pos++]);

        return cur;
    }


    // Returns true iff key and cur.key match from pos forward.  Used to complete exact matches in
    // possibly truncated subtrees.
    private boolean checkAbbreviated(final Bucket<?> cur, final char[] key, final int startPos) {
        final String value = cur.key;
        final int len = key.length;
        if (len != value.length()) return false;

        for (int pos = startPos; pos < len; pos++)
            if (key[pos] != value.charAt(pos)) return false;

        return true;
    }


    private static class Bucket<T> {
        char id;
        String key;
        T value;
        byte bucket;

        Bucket(final char id) {
            this.id = id;
            bucket = 0;

            //noinspection unchecked
            children = new Bucket[BucketList[bucket]]; // initialized to a nominal base capacity
        }

        Bucket<T> getChild(final char value) {
            final Bucket<T> out = children[value % children.length];
            return (out != null && out.id == value) ? out : null; // TODO: Can we avoid one or more of these checks?
        }

        Bucket<T> getOrAddChild(final char value) {
            // proceed down the tree, finding buckets until we find the right one, or hit bottom
            // if we hit bottom, add a new child storing the value of the string being added.

            while (true) {
                final int pos = value % children.length; // NOTE: rebalance() changes children.length
                final Bucket<T> out = children[pos];
                if (out == null) return children[pos] = new Bucket<>(value);
                if (out.id == value) return out;	// TODO: This condition can only be true the first time through
                rebalance(value);
            }

            // TODO: Implement truncated insertion
        }

        private void rebalance(final char value) {
            bucket++;

            @SuppressWarnings("unchecked")
            final Bucket<T>[] newChildren = new Bucket[BucketList[bucket]];	// should be physically limited to 64k children by the 16b nature of char

            final int newLength = newChildren.length;
            for (final Bucket<T> b : children) {
                final int newKey = b.id % newLength;
                if (newChildren[newKey] != null) {
                    // we have a new collision; need to start over again
                    rebalance(value); // yes, this is recursion, but it won't ever get more than around 20 frames deep in the very worst case.
                    return;
                }
                newChildren[newKey] = b;            //	place it in a new array in its new location
            }
            children = newChildren;							// replace the old bucket list with the new one
        }

        private Bucket<T>[] children;
    }

    // List of each prime higher that successive powers of 2, starting at 8
    // Chosen because of their ostensibly good collision behaviour when reassigning modulo indices
    // after growth.  This 'goodness' property is not proven in any way I know of, it just looked good
    // in a spreadsheet.
    // TODO: Expand to 64k, not 16k
    private static final int[] BucketList = new int[]{ 11, 37, 67, 131, 257, 521, 1049, 2053, 4099, 8209, 16384 };


    private Bucket<T> root;
}
