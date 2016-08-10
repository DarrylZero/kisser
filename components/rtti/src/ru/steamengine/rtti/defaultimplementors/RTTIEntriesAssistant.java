package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.ListSlot;
import ru.steamengine.rtti.basetypes.RTTIEntries;
import ru.steamengine.rtti.basetypes.RTTIEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Steam engine corp in 08.12.2009 22:14:57
 *
 * @author Christopher Marlowe
 */
public class RTTIEntriesAssistant implements RTTIEntries {

    private final List<RTTIEntry> properties = new ArrayList<RTTIEntry>();

    private final List<ListSlot> lists = new ArrayList<ListSlot>();

    @Override
    public RTTIEntry findEntry(String propertyName) {
        if (propertyName != null)
            for (RTTIEntry entry : properties)
                if (entry.getPropName().equals(propertyName))
                    return entry;
        return null;
    }

    @Override
    public Iterable<RTTIEntry> getProperties() {
        return new Iterable<RTTIEntry>() {
            @Override
            public Iterator<RTTIEntry> iterator() {
                return properties.iterator();
            }
        };
    }

    @Override
    public Iterable<ListSlot> getLists() {
        return new Iterable<ListSlot>() {
            @Override
            public Iterator<ListSlot> iterator() {
                return lists.iterator();
            }
        };
    }

    @Override
    public ListSlot findList(String propertyName) {
        if (propertyName != null)
            for (ListSlot slot : lists)
                if (slot.getPropertyName().equals(propertyName))
                    return slot;
        return null;
    }

    public void addEntries(RTTIEntries entries) {
        for (RTTIEntry entry : entries.getProperties())
            for (int i = properties.size() - 1; i >= 0; i--)
                if (properties.get(i).getPropName().equals(entry.getPropName()))
                    properties.remove(i);

        for (RTTIEntry entry : entries.getProperties())
            properties.add(entry);

        for (ListSlot item : entries.getLists())
            for (int i = lists.size() - 1; i >= 0; i--)
                if (lists.get(i).getPropertyName().equals(item.getPropertyName()))
                    lists.remove(i);

        for (ListSlot item : entries.getLists())
            lists.add(item);
    }
}
