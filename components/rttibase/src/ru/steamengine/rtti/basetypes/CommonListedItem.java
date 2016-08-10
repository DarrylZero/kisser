package ru.steamengine.rtti.basetypes;

/**
 * The interface of common use listed property
 *
 * Created by Steam engine corp. in 26.02.2011 01:14:06
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface CommonListedItem<ParentObject, Holder, Item> {
    /**
     * get all subling objects as an array
     *
     * @param object a "parent" object
     * @return all subling objects as an array.
     */
    Item[] getItems(Holder object);

    /**
     * begin updating
     *
     * @param object a holder
     */
    void beginUpdate(Holder object);


    /**
     * end updating
     *
     * @param object a holder
     */
    void endUpdate(Holder object);

    /**
     * clear all items
     *
     * @param holder a holder
     */
    void clear(Holder holder);


    /**
     * Create an Item .
     *
     * @param holder a holder object
     * @return added item (never Null)
     */
    Item newItem(Holder holder);


    /**
     * set an object
     *
     * @param holder  holder object
     * @param listObj object to set
     * @param index   index(0 is the first index)
     */
    void setItemValue(Holder holder, Item listObj, int index);


    /**
     * @param parentObject a parent object
     * @return holder a holder object
     */
    Holder getHolder(ParentObject parentObject);


    /**
     * returns a class of an item object
     *
     * @return a class of an item object(never is Null).
     */
    Class<Item> getItemClass();
}