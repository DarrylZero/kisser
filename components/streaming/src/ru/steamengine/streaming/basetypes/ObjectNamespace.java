package ru.steamengine.streaming.basetypes;

/**
 * an interface for object and their names
 * <p/>
 * Created by Steam engine corp. in 23.12.2009 0:33:00
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface ObjectNamespace {

    /**
     * set an object name
     *
     * @param obj  an object
     * @param name name.
     * @return name being set
     * @throws NullPointerException     is obj or name is null
     * @throws IllegalArgumentException if name is not unique
     */
    String setObjectName(Object obj, String name);

    /**
     * finds an object by its name
     *
     * @param name a name
     * @return object or null if not found
     */
    Object findObject(String name);


    /**
     * checks if a name unique for the instance of a namespace
     *
     * @param obj - object
     * @param name - name
     * @return true if a name unique for the instance of a namespace
     */
    boolean isNameUnique(Object obj, String name);


    /**
     * get an object name. if the name does not exist creates and adds a new one.
     *
     * @param obj    an object
     * @param prefix name prefix
     * @return name for an object( null is never returned)
     * @throws NullPointerException if one of arguments is null
     */
    String getObjectName(Object obj, String prefix);

    /**
     * Determines wether a name for an object unique.
     *
     * @param obj  an object
     * @param name a name for an object
     * @return true if so and false otherwise
     */
    // boolean isNameUnique(Object obj, String name);


    /**
     * clear all objects and its names
     */
    void clear();

}
