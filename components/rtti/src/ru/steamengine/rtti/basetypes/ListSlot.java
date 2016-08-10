package ru.steamengine.rtti.basetypes;

import java.util.Iterator;

/**
 * Created by Steam engine corp. in 27.02.2011 10:30:11
 *
 * @author Christopher Marlowe
 */
public interface ListSlot {

    /**
     * returns a name of the property
     *
     * @return a name of the property not null
     */
    String getPropertyName();

    /**
     * get all subling objects as an array
     *
     * @param object a "parent" object
     * @return all subling objects as an array.
     */
    @Deprecated
    Object[] getItems(Object object);

    /**
     * get all subling objects
     *
     * @param object a "parent" object
     * @return iterator through all subling objects
     */
    Iterator getIterator(Object object);


    /**
     * begin updating
     *
     * @param object a parent object
     */
    void beginUpdate(Object object);

    /**
     * end updating
     *
     * @param object a parent object
     */
    void endUpdate(Object object);

    /**
     * clear all items
     *
     * @param object a parent object
     */
    void clear(Object object);


    /**
     * Create an Item .
     *
     * @param object a parent object
     * @return added item (never Null)
     */
    Object newItem(Object object);


    /**
     * sets a value
     *
     * @param parentObject parent object
     * @param listObj      object to set
     * @param pos          pos(0 is the first position)
     */
    void setItemValue(Object parentObject, Object listObj, int pos);

    /**
     * returns a class of an item object
     *
     * @return a class of an item object(never is Null).
     */
    Class<?> getItemClass();
}
