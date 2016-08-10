package ru.steamengine.rtti.basetypes;

/**
 * Object structure handler interface.
 *
 *
 *
 * Created by Steam engine corp 03.12.2009 18:44:23
 *
 * Public API
 * @author Christopher Marlowe
 */
public interface ObjectProcessor<Parent>{    

    /**                 
     * Gets a group of subling objects.
     * This method is deprecated.
     * It still can be used but the implementor object should(must)also support
     * ObjectIterable interface for the better perfomance.
     * The gotten by iterator() method Iterator must return all subling objects one by one.
     *
     * @param obj an objects
     * @return group of objects as an array(if there are no objects - zero length array returned)
     */

    @Deprecated
    Object[] getObjectChildren(Parent obj);

    /**
     * sets a parent object 
     * @param object an object
     * @param parent  a parent object
     * @param position a position in parent object.
     * @return true if setting is successful .
     */
    boolean setChild(Object object, Parent parent, int position);
}
