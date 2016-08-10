package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 04.09.2010 18:47:41
 *
 * @author Christopher Marlowe
 */
public class Assert {
    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError with the given message.
     */
    public static void assertTrue(String message,
                                  boolean condition) {
        if (!condition)
            fail(message);
    }
    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError.
     */
    public static void assertTrue(boolean condition) {
        assertTrue(null, condition);
    }
    /**
     * Asserts that a condition is false. If it isn't it throws
     * an AssertionFailedError with the given message.
     */
    public static void assertFalse(String message,
                                   boolean condition) {
        assertTrue(message, !condition);
    }
    /**
     * Asserts that a condition is false. If it isn't it throws
     * an AssertionFailedError.
     */
    public static void assertFalse(boolean condition) {
        assertFalse(null, condition);
    }

    /**
     * Fails a test with the given message.
     */
    public static void fail(String message) {
        throw new AssertionFailedError(message);
    }
    /**
     * Fails a test with no message.
     */
    public static void fail() {
        fail(null);
    }


}
