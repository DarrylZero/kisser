package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 23.02.2010 15:42:10
 *
 * @author Christopher Marlowe
 */
public class ListedItemSlot {

    public final ListedItem item;

    public final String propertyName;

    public ListedItemSlot(String propertyName, ListedItem item) {
        if (item == null)
            throw new NullPointerException("item is null");
        if (propertyName == null)
            throw new NullPointerException("propertyName is null");
        this.item = item;
        this.propertyName = propertyName;
    }
}
