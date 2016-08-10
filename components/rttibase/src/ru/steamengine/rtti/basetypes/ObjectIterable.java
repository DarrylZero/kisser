package ru.steamengine.rtti.basetypes;

import java.util.Iterator;

/**
 * Created by Steam engine corp. in 10.04.2011 1:48:59
 *
 * @author Christopher Marlowe
 *         <p/>
 *         Public Api
 */
@Deprecated
public interface ObjectIterable<ParentObject, ListItem> {

    /**
     * Gets an iterator to iterate through the all subobjects
     *
     * @param parentObject parent objects
     * @return an iterator object
     */
    Iterator<ListItem> getIterator(ParentObject parentObject);
}
