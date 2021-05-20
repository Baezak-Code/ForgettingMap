package forgettingmap;

/**
 * The {@code MethodTracker} interface can be used to
 * track components of the underlying method.
 *
 * @param <K> Key that is going to be tracked.
 */
@FunctionalInterface
public interface MethodTracker<K> {

    /**
     * Tracks components of the given method.
     *
     * @param key Key that is going to be tracked.
     */
    void track(K key);
}
