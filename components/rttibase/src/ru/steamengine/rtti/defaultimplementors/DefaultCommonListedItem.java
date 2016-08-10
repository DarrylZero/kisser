package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.CommonListedItem;
import ru.steamengine.rtti.basetypes.ObjectIterable;
import ru.steamengine.rtti.basetypes.ListedItem;

import java.util.Iterator;

/**
 * Created by Steam engine corp. in 23.08.2010 23:07:41               
 * Public API
 *
 * @author Christopher Marlowe
 */
public class DefaultCommonListedItem<ParentObject, Holder, ListObj> implements
        CommonListedItem<ParentObject, Holder, ListObj>, ObjectIterable<Holder, ListObj> {


    @Override
    public ListObj[] getItems(Holder object) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void beginUpdate(Holder object) {
    }

    @Override
    public void endUpdate(Holder object) {
    }

    @Override
    public void clear(Holder object) {
    }

    @Override
    public ListObj newItem(Holder object) {
        return null;
    }

    @Override
    public void setItemValue(Holder parentObject, ListObj listObj, int index) {
    }

    @Override
    public Holder getHolder(ParentObject parentObject) {
        return null;
    }

    @Override
    public Class<ListObj> getItemClass() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Iterator getIterator(
            final Holder holder) {
        return new Iterator() {
            private final ListObj[] items = getItems(holder);
            private int pos = 0;
            @Override
            public boolean hasNext() {
                return pos < items.length;
            }

            @Override
            public Object next() {
                return items[pos++];  
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() is not supported");
            }
        };
    }
}