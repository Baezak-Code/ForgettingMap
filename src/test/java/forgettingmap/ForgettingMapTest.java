package forgettingmap;

import org.junit.Test;

import static org.junit.Assert.*;

public class ForgettingMapTest {

    private static final ForgettingMap<String, String> FORGETTING_MAP = new ForgettingMap<>(10);

    @Test
    public void testCreatingForgettingMapWithMaximumAssociations() {
        assertEquals(10, FORGETTING_MAP.getMaximumAssociations());
    }

    @Test(expected = NullPointerException.class)
    public void testAddThrowsNullPointerExceptionWhenKeyIsNull() {
        FORGETTING_MAP.add(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAddThrowsNullPointerExceptionWhenValueIsNull() {
        FORGETTING_MAP.add("key", null);
    }

    @Test
    public void testAddAndFind() {
        FORGETTING_MAP.add("key", "value");
        final String value = FORGETTING_MAP.find("key");
        assertNotNull(value);
        assertEquals("value", value);
    }

    @Test
    public void testAddingMoreAssociationsThanAllowedRemovesLeastUsed() {
        FORGETTING_MAP.add("key1", "value1");
        FORGETTING_MAP.find("key1");
        FORGETTING_MAP.add("key2", "value2");
        FORGETTING_MAP.find("key2");
        FORGETTING_MAP.find("key2");
        FORGETTING_MAP.add("key3", "value3");
        FORGETTING_MAP.find("key3");
        FORGETTING_MAP.find("key3");
        FORGETTING_MAP.add("key4", "value4");
        FORGETTING_MAP.find("key4");
        FORGETTING_MAP.find("key4");
        FORGETTING_MAP.add("key5", "value5");
        FORGETTING_MAP.find("key5");
        FORGETTING_MAP.find("key5");
        FORGETTING_MAP.add("key6", "value6");
        FORGETTING_MAP.find("key6");
        FORGETTING_MAP.find("key6");
        FORGETTING_MAP.add("key7", "value7");
        FORGETTING_MAP.find("key7");
        FORGETTING_MAP.find("key7");
        FORGETTING_MAP.add("key8", "value8");
        FORGETTING_MAP.find("key8");
        FORGETTING_MAP.find("key8");
        FORGETTING_MAP.add("key9", "value9");
        FORGETTING_MAP.find("key9");
        FORGETTING_MAP.find("key9");
        FORGETTING_MAP.add("key10", "value10");
        FORGETTING_MAP.find("key10");
        FORGETTING_MAP.find("key10");
        FORGETTING_MAP.add("key11", "value11");
        FORGETTING_MAP.find("key11");
        FORGETTING_MAP.find("key11");
        final String value = FORGETTING_MAP.find("key");
        assertNull(value);
    }

    @Test
    public void testFindReturnsNullWhenKeyValueAssociationDoesNotExist() {
        assertNull(FORGETTING_MAP.find("key12"));
    }
}
