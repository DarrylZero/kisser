package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 27.11.2010 0:52:53
 * <p/>
 * Public API
 *
 * Important note - every implementor MUST be thread safe
 * and stateless due to the fact that it is used in multy thread conditions.
 *
 * @author Christopher Marlowe
 */
public interface LoadNotifier<Obj> {

    /**
     * This method is called after ALL objects are read.
     *
     * @param object an object.
     */
    void objectLoaded(Obj object);

}
