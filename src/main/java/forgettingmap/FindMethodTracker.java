package forgettingmap;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code FindMethodTracker} is used to track everytime
 * a find operation is called from within the {@code ForgettingMap}.
 * It also handles the operation of removing the least-used value from
 * the {@code ForgettingMap}.
 *
 * @param <K> Key for the {@code ForgettingMap}.
 */
public final class FindMethodTracker<K> extends LinkedHashMap<K, AtomicInteger> implements MethodTracker<K>, Forgetting<K> {

    @Override
    public final void track(final K key) {
        if (key == null) {
            throw new NullPointerException("Key specified is null when trying to track the method call.");
        }
        doTrack(key);
    }

    /**
     * Performs the underlying tracking operation of when the find
     * operation was called.
     *
     * @param key Used to find the given value.
     */
    private void doTrack(final K key) {
        if (containsKey(key)) {
            get(key).getAndIncrement();
        } else {
            put(key, new AtomicInteger(1));
        }
    }

    @Override
    public final K removeLeastUsed() {
        final LinkedList<K> leastUsed = findAllOccurrencesOfMinimumValue(findMinimumValue());
        if (leastUsed.isEmpty()) {
            throw new IllegalStateException("Least used value not found.");
        }
        return removeIfNotNull(leastUsed);
    }

    /**
     * Removes the least used value from the {@link LinkedHashMap}.
     *
     * @param leastUsed Contains all the least used keys.
     * @return key that is going to be removed from the {@link ForgettingMap}.
     */
    private K removeIfNotNull(final LinkedList<K> leastUsed) {
        final K key = getLeastUsed(leastUsed);
        if (key == null) {
            throw new NullPointerException("Unable to find the least used key inside the method tracker.");
        }
        remove(key);
        return key;
    }

    /**
     * Returns the least-used key from within the tracker map.
     *
     * @param leastUsed Contains all the least used keys.
     * @return key that is going to be removed from the {@link ForgettingMap}.
     */
    private K getLeastUsed(final LinkedList<K> leastUsed) {
        return leastUsed.size() == 1 ? leastUsed.get(0) : leastUsed.removeLast();
    }

    /**
     * Finds all the occurrences of the minimum value inside the tracker map.
     *
     * @param min Value used to find all occurrences of the minimum value calculated.
     * @return {@link LinkedList} which contains all the least used keys.
     */
    private LinkedList<K> findAllOccurrencesOfMinimumValue(final int min) {
        final LinkedList<K> leastUsed = new LinkedList<>();
        forEach((key, value) -> {
            if (value.get() == min) {
                leastUsed.add(key);
            }
        });
        return leastUsed;
    }

    /**
     * Finds the minimum value from within the {@link LinkedHashMap}.
     *
     * @return value representing the minimum value from within the
     * {@link LinkedHashMap}.
     */
    private int findMinimumValue() {
        return Collections.min(entrySet(), Comparator.comparingInt(i -> i.getValue().get())).getValue().get();
    }
}
