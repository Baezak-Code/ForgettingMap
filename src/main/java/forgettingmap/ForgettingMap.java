package forgettingmap;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code ForgettingMap} is an extension of a {@link ConcurrentHashMap}
 * which can hold associations up to the limit specified inside the constructor
 * and is thread-safe. The reason it's called a {@code ForgettingMap} is that
 * once the limit of the map has been reached, it will remove the least-used
 * value, where least-used relates to the number of times each association has
 * been retrieved by the {@link ForgettingMap#find(Object)} operation.
 *
 * @param <K> Key for the {@code ForgettingMap}.
 * @param <V> Value for the {@code ForgettingMap}.
 */
public final class ForgettingMap<K, V> extends ConcurrentHashMap<K, V> {

    /**
     * The largest possible table capacity.  This value must be
     * exactly 1<<30 to stay within Java array allocation and indexing
     * bounds for power of two table sizes, and is further required
     * because the top two bits of 32bit hash fields are used for
     * control purposes.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private final int maximumAssociations;

    private final transient FindMethodTracker<K> findMethodTracker;

    /**
     * Creates a new instance of {@code ForgettingMap} with
     * the specified <code>maximumAssociations</code> allowed.
     *
     * @param maximumAssociations The maximum associations allowed
     *                            inside the map at any given point
     *                            in time.
     */
    public ForgettingMap(final int maximumAssociations) {
        this.maximumAssociations = Math.min(maximumAssociations, MAXIMUM_CAPACITY);
        findMethodTracker = new FindMethodTracker<>();
    }

    /**
     * Maps the specified key to the specified value in this table.
     * Neither the key nor the value can be null. If the size of
     * the {@code ForgettingMap} is equal too or larger than the
     * {@code maximumAssociations} specified when attempting to add
     * a new value to the map then the association that is least-used
     * is removed. If multiple least-used values exist inside the map
     * then the last least-used entry will be removed to clear up space
     * for the new addition.
     *
     * <p>The value can be retrieved by calling the {@code get} method
     * with a key that is equal to the original key.</p>
     *
     * @param key Key with which the specified value is to be associated.
     * @param value Value to be associated with the specified key.
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     *
     * @throws NullPointerException If the specified key or value is null.
     */
    public final V add(final K key, final V value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key or value is null when trying to add to the map.");
        }
        if (size() >= maximumAssociations) {
            remove(findMethodTracker.removeLeastUsed());
        }
        return put(key, value);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code key.equals(k)},
     * then this method returns {@code v}; otherwise it returns
     * {@code null}. There can be at most one such mapping.
     *
     * @param key Used to find the value inside the {@code ForgettingMap}.
     * @return value which corresponds to the <code>key</code> provided.
     *
     * @throws NullPointerException If the specified key is null.
     */
    public final V find(final K key) {
        final V value = get(key);
        if (value != null) {
            findMethodTracker.track(key);
        }
        return value;
    }

    /**
     * Returns the maximum associations that states
     * the limit of the {@code ForgettingMap}.
     *
     * @return value representing the maximum associations
     * that states the limit of the {@code ForgettingMap}.
     */
    public final int getMaximumAssociations() {
        return maximumAssociations;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final ForgettingMap<?, ?> that = (ForgettingMap<?, ?>) o;
        return maximumAssociations == that.maximumAssociations &&
                Objects.equals(findMethodTracker, that.findMethodTracker);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(super.hashCode(), maximumAssociations, findMethodTracker);
    }
}
