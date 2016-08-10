package ru.steamengine.rtti.basetypes;

import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.getListedItemType;

import java.util.Iterator;

/**
 * Created by Steam engine corp. in 27.02.2011 10:42:36
 *
 * @author Christopher Marlowe
 */
public class CommonListedItemSlot implements ListSlot {
    private final CommonListedItem item;
    private final String propertyName;
    private final Class<?> clazz;

    public CommonListedItemSlot(CommonListedItem item,
                                String propertyName) {
        if (item == null)
            throw new NullPointerException("item is null");
        if (propertyName == null)
            throw new NullPointerException("propertyName is null");
        this.item = item;
        this.propertyName = propertyName;
        this.clazz = getListedItemType(item);
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Object[] getItems(Object object) {
        return item.getItems(holder(object));
    }

    private Object holder(Object object) {
        return item.getHolder(object);
    }

    @Override
    public void beginUpdate(Object object) {
        item.beginUpdate(holder(object));
    }

    @Override
    public void endUpdate(Object object) {
        item.endUpdate(holder(object));
    }

    @Override
    public void clear(Object object) {
        item.clear(holder(object));
    }

    @Override
    public Object newItem(Object object) {
        return item.newItem(holder(object));
    }

    @Override
    public void setItemValue(Object object, Object listObj, int index) {
        item.setItemValue(holder(object), listObj, index);
    }

    @Override
    public Class<?> getItemClass() {
        return clazz;
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
                    return items[pos++];
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("remove() is not supported");
                }
            };
        }
    }
}
