/**
 * Created by Steam engine corp. in 08.10.2011 21:33:22
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.basetypes;

/**
 * An object name processor interface.
 * it is used to associate an object with a name.
 * <p/>
 * Implemening classes must be thread safe and/or stateless
 *
 * PUBLIC API
 *
 * @param <T>
 */
public interface ObjectNameProcessor<T> {

    /**
     * gets name of an object
     *
     * @param t an object - musn't ever be null
     * @return an object name. - if returned value is null or a value that is already in use a new name is issued for
     *         and set for an object via setName() method.
     */
    String getName(T t);

    /**
     * Sets name for an object
     *
     * @param t an object - musn't ever be null
     * @param name - a name for an object.Null should never be passed to this method.
     *             Each implementor must set a field of an object's field.
     */
    void setName(T t, String name);


}
