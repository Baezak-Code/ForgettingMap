package forgettingmap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FindMethodTrackerTest {

    @Test(expected = NullPointerException.class)
    public void testTrackThrowsNullPointerExceptionWhenKeyIsNull() {
        new FindMethodTracker<>().track(null);
    }

    @Test
    public void testTrackOnNewKey() {
        final FindMethodTracker<String> findMethodTracker = new FindMethodTracker<>();
        findMethodTracker.track("key");
        assertEquals(1, findMethodTracker.get("key").get());
    }

    @Test
    public void testTrackOnExistingKey() {
        final FindMethodTracker<String> findMethodTracker = new FindMethodTracker<>();
        findMethodTracker.track("key");
        findMethodTracker.track("key");
        assertEquals(2, findMethodTracker.get("key").get());
    }

    @Test
    public void testRemoveLeastUsed() {
        final FindMethodTracker<String> findMethodTracker = new FindMethodTracker<>();
        findMethodTracker.track("key");
        findMethodTracker.track("key");
        findMethodTracker.track("key2");
        findMethodTracker.removeLeastUsed();
        assertNull(findMethodTracker.get("key2"));
    }

    @Test
    public void testRemoveLeastUsedWhenMultipleOccurrencesExist() {
        final FindMethodTracker<String> findMethodTracker = new FindMethodTracker<>();
        findMethodTracker.track("key");
        findMethodTracker.track("key");
        findMethodTracker.track("key2");
        findMethodTracker.track("key3");
        findMethodTracker.track("key4");
        findMethodTracker.removeLeastUsed();
        assertNull(findMethodTracker.get("key4"));
    }
}
