package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.ListedItem;
import ru.steamengine.rtti.basetypes.ListedItemSlot;
import ru.steamengine.rtti.basetypes.RTTIEntries;
import ru.steamengine.rtti.basetypes.RTTIEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Steam engine corp in 08.12.2009 22:14:57
 *
 * @author Christopher Marlowe
 */
public class RTTIEntriesAssistant implements RTTIEntries {


    @Override
    public RTTIEntry findEntry(String propertyName) {

        if (propertyName != null)
            for (RTTIEntry entry : list)
                if (entry.getPropName().equals(propertyName))
                    return entry;
        return null;
    }

    private List<RTTIEntry> list = new ArrayList<RTTIEntry>();

    private List<ListedItemSlot> listedItems = new ArrayList<ListedItemSlot>();

    @Override
    public RTTIEntry[] getEntries() {
        return list.toArray(new RTTIEntry[list.size()]);
    }

    @Override
    public ListedItem findListedItem(String propertyName) {
        if (propertyName != null)
            for (ListedItemSlot slot : listedItems)
                if (slot.propertyName.equals(propertyName))
                    return slot.item;
        return null;
   }

    @Override
    public ListedItemSlot[] getListedItems() {
        return listedItems.toArray(new ListedItemSlot[listedItems.size()]);  
    }


    public void addEntries(RTTIEntries entries) {
        RTTIEntry[] entryList = entries.getEntries();
        for (RTTIEntry entry : entryList)
            for (int i = list.size() - 1; i >= 0; i--)
                if (list.get(i).getPropName().equals(entry.getPropName()))
                    list.remove(i);
        list.addAll(Arrays.asList(entryList));

        ListedItemSlot[] items = entries.getListedItems();
        for (ListedItemSlot item : items)
            for (int i = listedItems.size() - 1; i >= 0; i--)
                if (listedItems.get(i).propertyName.equals(item.propertyName))
                    listedItems.remove(i);           
        listedItems.addAll(Arrays.asList(items));
    }

}
