package ru.steamengine.rtti.defaultimplementors.secondregistryfactory;

import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.basetypes.additionalproperties.*;
import ru.steamengine.rtti.defaultimplementors.BaseInfoParser;
import ru.steamengine.rtti.defaultimplementors.DefaultPropInfoParser;
import ru.steamengine.rtti.defaultimplementors.additionalproperties.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Steam engine corp in 26.10.2009 23:22:51
 *
 * @author Christopher Marlowe
 */

// describe
public class SecondClassPropRegistrySlot implements ClassPropRegistrySlot, RTTIEntries {
    private final Class clazz;
    private final MultyReadExclusiveWriteLock lock;
    private final SimpleEvent event;
    private final Registry registry;
    private final List<RTTIEntry> properties = new ArrayList<RTTIEntry>();
    private final List<ListSlot> lists = new ArrayList<ListSlot>();

    public SecondClassPropRegistrySlot(Class clazz,
                                       MultyReadExclusiveWriteLock lock,
                                       SimpleEvent event,
                                       Registry registry) {
        if (clazz == null)
            throw new IllegalArgumentException("clazz is null");
        if (lock == null)
            throw new IllegalArgumentException("lock is null");
        if (registry == null)
            throw new IllegalArgumentException("registry is null");

        this.clazz = clazz;
        this.lock = lock;
        this.event = event;
        this.registry = registry;
    }


    public ClassPropRegistrySlot regProp(String propInfo) {
        lock.lockWrite();
        try {
            BufferedReader reader = new BufferedReader(new StringReader(propInfo));
            try {
                String line;
                while ((line = reader.readLine()) != null)
                    if (!line.trim().isEmpty())
                        doRegInfo(line);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        } finally {
            lock.unlockWrite();
        }
        return this;
    }


    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, LongProperty property) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            LongRTTIAdapter info = new LongRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }

        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, BinaryProperty property) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            BinaryRTTIAdapter info = new BinaryRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }


    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, IntegerProperty property) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            IntegerRTTIAdapter info = new IntegerRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, ByteProperty property) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            ByteRTTIAdapter info = new ByteRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, ShortProperty property) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            ShortRTTIAdapter info = new ShortRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, StringProperty property) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            StringRTTIAdapter info = new StringRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, CharProperty property) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            CharRTTIAdapter info = new CharRTTIAdapter(propertyName, property);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @Override
    public ClassPropRegistrySlot regProp(String propertyName, ListedItem listedItem) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            ListSlot slot = new ListedItemSlot(propertyName, listedItem);
            addListedItem(slot);
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @Override
    public ClassPropRegistrySlot regProp(String propertyName,
                                         CommonListedItem listedItem) {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            removePropItem(propertyName);
            removeListedItem(propertyName);

            CommonListedItemSlot slot = new CommonListedItemSlot(listedItem, propertyName);
            addListedItem(slot);
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    private void addListedItem(ListSlot slot) {
        lists.add(slot);
    }


    @Override
    public <Result, Obj> ClassPropRegistrySlot regProp(String propertyName,
                                                       Class<Result> propertyClass,
                                                       ROObjectProperty<Result, Obj> property)
            throws IllegalArgumentException {

        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            ROObjectPropertyAdapter<Result, Obj> info =
                    new ROObjectPropertyAdapter<Result, Obj>(propertyName, property, propertyClass);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @Override
    public <Result, Obj> ClassPropRegistrySlot regProp(String propertyName,
                                                       Class<Result> propertyClass,
                                                       RWObjectProperty<Result, Obj> property)
            throws IllegalArgumentException {
        lock.lockWrite();
        try {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            RWObjectPropertyAdapter<Result, Obj> info =
                    new RWObjectPropertyAdapter<Result, Obj>(propertyName, property, propertyClass);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        } finally {
            lock.unlockWrite();
        }
        return this;
    }

    @Override
    public ListSlot findList(String propertyName) {
        lock.lockRead();
        try {
            for (ListSlot slot : lists)
                if (slot.getPropertyName().equals(propertyName))
                    return slot;
        } finally {
            lock.unlockRead();
        }
        return null;
    }

    @Override
    public Iterable<ListSlot> getLists() {
        return new Iterable<ListSlot>() {
            @Override
            public Iterator<ListSlot> iterator() {
                lock.lockRead();
                try {
                    return new ArrayList<ListSlot>(lists).iterator();
                } finally {
                    lock.unlockRead();
                }
            }
        };
    }


    public boolean checkInfo(String propInfo) {
        return BaseInfoParser.checkInfo(clazz, propInfo);
    }

    @Override
    public RTTIEntry findEntry(String propertyName) {
        lock.lockRead();
        try {
            for (RTTIEntry entry : properties)
                if (entry.getPropName().equals(propertyName))
                    return entry;
        } finally {
            lock.unlockRead();
        }
        return null;
    }

    @Override
    public ClassPropRegistrySlot nop() {
        return this;
    }

    @Override
    public Iterable<RTTIEntry> getProperties() {
        return new Iterable<RTTIEntry>() {
            @Override
            public Iterator<RTTIEntry> iterator() {
                lock.lockRead();
                try {
                    return new ArrayList<RTTIEntry>(properties).iterator();
                } finally {
                    lock.unlockRead();
                }
            }
        };
    }

    public final Registry registry() {
        return registry;
    }

    private void removePropItem(RTTIEntry item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");
        removePropItem(item.getPropName());
    }

    private void removePropItem(String itemName) {
        if (itemName == null)
            throw new IllegalArgumentException("itemName is null");

        for (int i = properties.size() - 1; i >= 0; i--)
            if (properties.get(i).getPropName().equals(itemName))
                properties.remove(i);

    }

    private void removeListedItem(String propertyName) {
        for (int i = lists.size() - 1; i >= 0; i--)
            if (lists.get(i).getPropertyName().equals(propertyName))
                lists.remove(i);
    }

    private void addItem(RTTIEntry item) {
        properties.add(item);
    }

    private void fireChangeEvent() {
        if (event != null)
            event.fire();
    }


    private void doRegInfo(String propInfo) {
        RTTIEntry info = DefaultPropInfoParser.INSTANCE.parseInfo(clazz, propInfo);
        removePropItem(info.getPropName());
        removeListedItem(info.getPropName());
        addItem(info);
        fireChangeEvent();
    }


}