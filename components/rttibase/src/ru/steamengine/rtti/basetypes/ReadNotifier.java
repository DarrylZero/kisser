/**
 * Created by Steam engine corp. in 21.10.2011 22:39:35
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 27.11.2010 0:52:53
 * <p/>
 * Public API
 * <p/>
 * Important note - every implementor MUST be thread safe
 * and stateless due to the fact that it is used in multy thread conditions.
 *
 * @author Christopher Marlowe
 */
public interface ReadNotifier<Obj> {

    public static enum ReadMode {
        designtime,
        runtime
    }

    /**
     * This method is called before ALL properties are read.just after an object is created.
     *
     * @param object an object.
     * @param readMode read mode
     */
    void onObjectRead(Obj object, ReadMode readMode);


}
