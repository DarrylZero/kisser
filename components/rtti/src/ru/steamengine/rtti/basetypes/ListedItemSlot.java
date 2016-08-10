package ru.steamengine.rtti.basetypes;

import ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.getListedItemType;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Steam engine corp. in 27.02.2011 11:42:34
 *
 * @author Christopher Marlowe
 */
public class ListedItemSlot implements ListSlot {

    private final ListedItem item;
    private final String propertyName;
    private final Class<?> clazz;

    public String getPropertyName() {
        return propertyName;
    }

    public ListedItemSlot(String propertyName,
                          ListedItem item) {
        if (item == null)
            throw new NullPointerException("item is null");
        if (propertyName == null)
            throw new NullPointerException("propertyName is null");
        this.item = item;
        this.propertyName = propertyName;
        this.clazz = getListedItemType(item);
    }

    @Override
    public Object[] getItems(Object o) {
        return item.getItems(o);
    }

    @Override
    public Iterator getIterator(
            final Object parent) {
        if (item instanceof ObjectIterable) {
            ObjectIterable iterable = (ObjectIterable) item;
            return iterable.getIterator(parent);
        } else {
            return new Iterator() {
                private final Object[] items = getItems(parent);
                private int pos = 0;

                @Override
                public boolean hasNext() {
                    return pos < items.length;
                }

                @Override
                public Object next() {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return items[pos++];
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("remove() is not supported");
                }
            };
        }
    }

    @Override
    public void beginUpdate(Object o) {
        //noinspection unchecked
        item.beginUpdate(o);
    }

    @Override
    public void endUpdate(Object o) {
        //noinspection unchecked
        item.endUpdate(o);
    }

    @Override
    public void clear(Object o) {
        //noinspection unchecked
        item.clear(o);
    }

    @Override
    public Object newItem(Object o) {
        //noinspection unchecked
        return item.newItem(o);
    }

    @Override
    public void setItemValue(Object o, Object o1, int index) {
        //noinspection unchecked
        item.setItemValue(o, o1, index);
    }

    @Override
    public Class<?> getItemClass() {
        return clazz;
    }
}
