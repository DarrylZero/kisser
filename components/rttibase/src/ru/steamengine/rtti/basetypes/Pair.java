package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 24.11.2010 22:43:50
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface Pair<ObjectOne, ObjectTwo> {

    /**
     * @return first object
     */
    ObjectOne getObjOne();

    /**
     * @return second object
     */
    ObjectTwo getObjTwo();
}
