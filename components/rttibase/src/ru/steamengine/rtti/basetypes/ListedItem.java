package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 26.12.2009 23:31:06
 *
 * Implementors can also support ObjectIterable interface for better perfomance
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface ListedItem<ParentObject, ListObj> {

    /**
     * get all subling objects as an array
     *
     * @param object a "parent" object
     * @return all subling objects as an array.
     */
    @Deprecated
    ListObj[] getItems(ParentObject object);

    /**
     * begin updating
     *
     * @param object a parent object
     */
    void beginUpdate(ParentObject object);


    /**
     * end updating
     *
     * @param object a parent object
     */
    void endUpdate(ParentObject object);

    /**
     * clear all items
     *
     * @param object a parent object
     */
    void clear(ParentObject object);


    /**
     * Create an Item .
     * This method must be declared in every describing class even
     * though the class is inherited from the base one.
     * @param object a parent object
     * @return added item (never Null)
     */
    ListObj newItem(ParentObject object);


    /**
     * set an object 
     *
     * @param parentObject  parent object
     * @param listObj object to set
     * @param index  index(0 is the first index)
     */
    void setItemValue(ParentObject parentObject, ListObj listObj, int index);


    /**
     * returns a class of an item object
     *
     * @return a class of an item object(never is Null).
     */
    Class<ListObj> getItemClass();
}
