package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.ListedItem;
import ru.steamengine.rtti.basetypes.ObjectIterable;

import java.util.Iterator;

/**
 * Created by Steam engine corp. in 23.08.2010 23:07:41
 * Public API
 *
 * @author Christopher Marlowe
 */
public class DefaultListedItem<ParentObject, ListObj> implements
        ListedItem<ParentObject, ListObj>, ObjectIterable<ParentObject, ListObj> {

    @Override
    @Deprecated
    public ListObj[] getItems(ParentObject object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<ListObj> getIterator(
            final ParentObject parentObject) {

        return new Iterator<ListObj>() {
            private final ListObj[] items = getItems(parentObject);
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < items.length;
            }

            @Override
            public ListObj next() {
                return items[pos++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() is not supported");
            }
        };
    }

    @Override
    public void beginUpdate(ParentObject object) {
    }

    @Override
    public void endUpdate(ParentObject object) {
    }

    @Override
    public void clear(ParentObject object) {
    }

    @Override
    public ListObj newItem(ParentObject object) {
        return null;
    }

    @Override
    public void setItemValue(ParentObject object, ListObj listObj, int index) {
    }

    @Override
    public Class<ListObj> getItemClass() {
        return null;
    }

}
