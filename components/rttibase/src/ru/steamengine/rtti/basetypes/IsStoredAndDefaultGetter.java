package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp in 13.10.2009 23:16:21
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface IsStoredAndDefaultGetter {

    /**
     * get value
     *
     * @param object object
     * @return value of the flag
     */
    boolean getValue(Object object);

    public static final IsStoredAndDefaultGetter ALWAYS_TRUE = new IsStoredAndDefaultGetter() {
        @Override
        public boolean getValue(Object object) {
            return true;
        }
    };

    public static final IsStoredAndDefaultGetter NEVER_TRUE = new IsStoredAndDefaultGetter() {
        @Override
        public boolean getValue(Object object) {
            return false;
        }
    };


}
