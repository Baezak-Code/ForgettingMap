package forgettingmap;

/**
 * The {@code Forgetting} interface can be used when needing
 * to remove an entry that is least used.
 *
 * @param <K> Key that is the least-used.
 */
@FunctionalInterface
public interface Forgetting<K> {

    /**
     * Removes the least used value from the collection.
     *
     * @return key that is least-used.
     */
    K removeLeastUsed();
}
