/**
 * Created by Steam engine corp. in 26.10.2011 21:35:04
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 27.11.2010 0:52:53
 * <p/>
 * Public API
 *
 * Important note - every implementor MUST be thread safe
 * and/or stateless due to the fact that it is used in multy thread conditions.
 *
 * @author Christopher Marlowe
 */

public interface ShutDownNotifier<Obj>{
        
    /**
     * This method is called before the object is shut down.
     *
     * @param object an object.
     */
    void shutingDown(Obj object);

}

